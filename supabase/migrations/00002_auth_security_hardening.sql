-- Office Mitra - Authentication & Row Level Security hardening
--
-- Safe to run more than once. Nothing is dropped except policies and functions that are
-- re-created immediately below; no table, column or row is deleted or rewritten.
--
-- What it fixes (all found while auditing 00001_initial_schema.sql):
--   * profiles: users could change server-controlled columns (account_status, email) on their own
--     row; there was no INSERT policy for the app's "create my missing profile" fallback.
--   * companies: every signed-in user could list every company, including all join codes.
--   * company_memberships: a user could insert their own membership as already 'approved'.
--   * attendance / leave_requests / approval_requests: rows could be written for a company the
--     user is not an approved member of; a user could approve their own leave request.
--   * notifications: UPDATE had no WITH CHECK; the app's delete had no policy and did nothing.
--   * helper functions could be called through the API to read any user's company and role.
--   * attendance photos: a manager of any company could read the photos of every company.

-- ============================================================================
-- 1. HELPER FUNCTIONS
-- ============================================================================

-- The helpers used by the policies run as SECURITY DEFINER so that policies on
-- company_memberships do not recurse. They now answer only for the calling user, so they can no
-- longer be used through the API to look up somebody else's company or role.
CREATE OR REPLACE FUNCTION public.get_user_company_id(p_user_id UUID)
RETURNS UUID AS $$
    SELECT company_id FROM public.company_memberships
    WHERE user_id = p_user_id
      AND p_user_id = auth.uid()
      AND status = 'approved'
    ORDER BY created_at, id
    LIMIT 1;
$$ LANGUAGE sql STABLE SECURITY DEFINER SET search_path = public;

CREATE OR REPLACE FUNCTION public.get_user_role(p_user_id UUID)
RETURNS TEXT AS $$
    SELECT role FROM public.company_memberships
    WHERE user_id = p_user_id
      AND p_user_id = auth.uid()
      AND status = 'approved'
    ORDER BY created_at, id
    LIMIT 1;
$$ LANGUAGE sql STABLE SECURITY DEFINER SET search_path = public;

CREATE OR REPLACE FUNCTION public.is_manager_or_higher(p_user_id UUID)
RETURNS BOOLEAN AS $$
    SELECT EXISTS (
        SELECT 1 FROM public.company_memberships
        WHERE user_id = p_user_id
          AND p_user_id = auth.uid()
          AND status = 'approved'
          AND role IN ('manager', 'hr', 'admin')
    );
$$ LANGUAGE sql STABLE SECURITY DEFINER SET search_path = public;

-- True when the calling user is an approved member of the given company.
CREATE OR REPLACE FUNCTION public.is_approved_member(p_company_id UUID)
RETURNS BOOLEAN AS $$
    SELECT EXISTS (
        SELECT 1 FROM public.company_memberships
        WHERE user_id = auth.uid()
          AND company_id = p_company_id
          AND status = 'approved'
    );
$$ LANGUAGE sql STABLE SECURITY DEFINER SET search_path = public;

-- True when the calling user is a manager, HR or admin of the given company.
CREATE OR REPLACE FUNCTION public.is_company_manager(p_company_id UUID)
RETURNS BOOLEAN AS $$
    SELECT EXISTS (
        SELECT 1 FROM public.company_memberships
        WHERE user_id = auth.uid()
          AND company_id = p_company_id
          AND status = 'approved'
          AND role IN ('manager', 'hr', 'admin')
    );
$$ LANGUAGE sql STABLE SECURITY DEFINER SET search_path = public;

-- Finds one active company by its exact join code. Replaces the policy that let every signed-in
-- user read the whole companies table. Returns nothing to callers that are not signed in.
CREATE OR REPLACE FUNCTION public.lookup_company_by_code(p_code TEXT)
RETURNS TABLE (id UUID, name TEXT, code TEXT, address TEXT, status TEXT) AS $$
    SELECT c.id, c.name, c.code, c.address, c.status
    FROM public.companies c
    WHERE auth.uid() IS NOT NULL
      AND c.status = 'active'
      AND c.code = btrim(p_code)
    LIMIT 1;
$$ LANGUAGE sql STABLE SECURITY DEFINER SET search_path = public;

-- The five policy helpers stay executable by `anon`: policies are evaluated for that role too,
-- and a missing EXECUTE privilege would turn "no rows" into a permission error. They return
-- nothing for a caller without a session, because they only answer for auth.uid().
REVOKE ALL ON FUNCTION public.get_user_company_id(UUID) FROM PUBLIC;
REVOKE ALL ON FUNCTION public.get_user_role(UUID) FROM PUBLIC;
REVOKE ALL ON FUNCTION public.is_manager_or_higher(UUID) FROM PUBLIC;
REVOKE ALL ON FUNCTION public.is_approved_member(UUID) FROM PUBLIC;
REVOKE ALL ON FUNCTION public.is_company_manager(UUID) FROM PUBLIC;
REVOKE ALL ON FUNCTION public.lookup_company_by_code(TEXT) FROM PUBLIC, anon;

GRANT EXECUTE ON FUNCTION public.get_user_company_id(UUID) TO anon, authenticated, service_role;
GRANT EXECUTE ON FUNCTION public.get_user_role(UUID) TO anon, authenticated, service_role;
GRANT EXECUTE ON FUNCTION public.is_manager_or_higher(UUID) TO anon, authenticated, service_role;
GRANT EXECUTE ON FUNCTION public.is_approved_member(UUID) TO anon, authenticated, service_role;
GRANT EXECUTE ON FUNCTION public.is_company_manager(UUID) TO anon, authenticated, service_role;
GRANT EXECUTE ON FUNCTION public.lookup_company_by_code(TEXT) TO authenticated, service_role;

-- ============================================================================
-- 2. PROFILES
-- ============================================================================

-- A second profile row for the same account is impossible (the primary key is the auth user id).
-- ON CONFLICT makes the trigger harmless when the row already exists, so that creating the auth
-- account can never fail because of it.
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO public.profiles (id, full_name, email, phone_number)
    VALUES (
        NEW.id,
        COALESCE(NULLIF(btrim(NEW.raw_user_meta_data->>'full_name'), ''), 'Employee'),
        NEW.email,
        COALESCE(NEW.raw_user_meta_data->>'phone_number', '')
    )
    ON CONFLICT (id) DO NOTHING;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER SET search_path = public;

DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();

-- profiles.email is a copy of the login email. It is written by the server only.
CREATE OR REPLACE FUNCTION public.handle_user_email_change()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE public.profiles SET email = NEW.email, updated_at = now() WHERE id = NEW.id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER SET search_path = public;

DROP TRIGGER IF EXISTS on_auth_user_email_changed ON auth.users;
CREATE TRIGGER on_auth_user_email_changed
    AFTER UPDATE OF email ON auth.users
    FOR EACH ROW
    WHEN (OLD.email IS DISTINCT FROM NEW.email)
    EXECUTE FUNCTION public.handle_user_email_change();

-- When the app has to create a missing profile row itself, the email is filled in from the auth
-- account instead of being trusted from the client.
CREATE OR REPLACE FUNCTION public.profiles_fill_server_columns()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.email IS NULL THEN
        SELECT u.email INTO NEW.email FROM auth.users u WHERE u.id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER SET search_path = public;

DROP TRIGGER IF EXISTS profiles_fill_server_columns ON public.profiles;
CREATE TRIGGER profiles_fill_server_columns
    BEFORE INSERT ON public.profiles
    FOR EACH ROW EXECUTE FUNCTION public.profiles_fill_server_columns();

-- Column privileges: a signed-in user may set only their name, phone number and avatar. The
-- account status, the email copy and the timestamps cannot be written through the API at all.
REVOKE INSERT, UPDATE ON public.profiles FROM anon, authenticated;
GRANT INSERT (id, full_name, phone_number, avatar_path) ON public.profiles TO authenticated;
GRANT UPDATE (full_name, phone_number, avatar_path) ON public.profiles TO authenticated;

DROP POLICY IF EXISTS "Users can create their own profile" ON public.profiles;
CREATE POLICY "Users can create their own profile"
    ON public.profiles FOR INSERT
    TO authenticated
    WITH CHECK (auth.uid() = id);

DROP POLICY IF EXISTS "Users can update their own profile" ON public.profiles;
CREATE POLICY "Users can update their own profile"
    ON public.profiles FOR UPDATE
    TO authenticated
    USING (auth.uid() = id)
    WITH CHECK (auth.uid() = id);

-- ============================================================================
-- 3. COMPANIES
-- ============================================================================

-- Replaced by public.lookup_company_by_code(): one company, by exact code, nothing else.
DROP POLICY IF EXISTS "Authenticated users can lookup company code" ON public.companies;

-- A user can see the company they applied to (also while pending or rejected) and the company
-- they own, so the app can show its name on the status screen.
DROP POLICY IF EXISTS "Applicants and owners can view their company" ON public.companies;
CREATE POLICY "Applicants and owners can view their company"
    ON public.companies FOR SELECT
    TO authenticated
    USING (
        owner_id = auth.uid()
        OR id IN (
            SELECT company_id FROM public.company_memberships
            WHERE user_id = auth.uid()
        )
    );

-- A company can only be created with the creator as its owner.
DROP POLICY IF EXISTS "Authenticated users can create companies" ON public.companies;
CREATE POLICY "Authenticated users can create companies"
    ON public.companies FOR INSERT
    TO authenticated
    WITH CHECK (owner_id = auth.uid());

-- ============================================================================
-- 4. COMPANY MEMBERSHIPS
-- ============================================================================

-- New memberships are requests. They become 'approved' only by a privileged update.
ALTER TABLE public.company_memberships ALTER COLUMN status SET DEFAULT 'pending';

DROP POLICY IF EXISTS "Users can apply to join company" ON public.company_memberships;
CREATE POLICY "Users can apply to join company"
    ON public.company_memberships FOR INSERT
    TO authenticated
    WITH CHECK (
        user_id = auth.uid()
        AND role = 'employee'
        AND status = 'pending'
    );

-- ============================================================================
-- 5. ATTENDANCE
-- ============================================================================

DROP POLICY IF EXISTS "Users can punch in/out for themselves" ON public.attendance;
CREATE POLICY "Users can punch in/out for themselves"
    ON public.attendance FOR INSERT
    TO authenticated
    WITH CHECK (user_id = auth.uid() AND public.is_approved_member(company_id));

DROP POLICY IF EXISTS "Users can update their own attendance punch" ON public.attendance;
CREATE POLICY "Users can update their own attendance punch"
    ON public.attendance FOR UPDATE
    TO authenticated
    USING (user_id = auth.uid())
    WITH CHECK (user_id = auth.uid() AND public.is_approved_member(company_id));

-- ============================================================================
-- 6. LEAVE REQUESTS
-- ============================================================================

DROP POLICY IF EXISTS "Users can create leave requests for themselves" ON public.leave_requests;
CREATE POLICY "Users can create leave requests for themselves"
    ON public.leave_requests FOR INSERT
    TO authenticated
    WITH CHECK (
        user_id = auth.uid()
        AND status = 'Pending'
        AND public.is_approved_member(company_id)
    );

-- The owner of a request may only cancel it. Without WITH CHECK the same policy allowed the
-- owner to set their own request to 'Approved'.
DROP POLICY IF EXISTS "Users can cancel their own pending leave requests" ON public.leave_requests;
CREATE POLICY "Users can cancel their own pending leave requests"
    ON public.leave_requests FOR UPDATE
    TO authenticated
    USING (user_id = auth.uid() AND status = 'Pending')
    WITH CHECK (user_id = auth.uid() AND status IN ('Pending', 'Cancelled'));

DROP POLICY IF EXISTS "Managers can update/review leave requests" ON public.leave_requests;
CREATE POLICY "Managers can update/review leave requests"
    ON public.leave_requests FOR UPDATE
    TO authenticated
    USING (public.is_company_manager(company_id))
    WITH CHECK (public.is_company_manager(company_id));

-- ============================================================================
-- 7. APPROVAL REQUESTS
-- ============================================================================

DROP POLICY IF EXISTS "Users can submit approval requests" ON public.approval_requests;
CREATE POLICY "Users can submit approval requests"
    ON public.approval_requests FOR INSERT
    TO authenticated
    WITH CHECK (
        user_id = auth.uid()
        AND status = 'Pending'
        AND public.is_approved_member(company_id)
    );

-- ============================================================================
-- 8. NOTIFICATIONS
-- ============================================================================

DROP POLICY IF EXISTS "Users can update their own notification read status" ON public.notifications;
CREATE POLICY "Users can update their own notification read status"
    ON public.notifications FOR UPDATE
    TO authenticated
    USING (user_id = auth.uid())
    WITH CHECK (user_id = auth.uid());

DROP POLICY IF EXISTS "Users can delete their own notifications" ON public.notifications;
CREATE POLICY "Users can delete their own notifications"
    ON public.notifications FOR DELETE
    TO authenticated
    USING (user_id = auth.uid());

-- ============================================================================
-- 9. STORAGE: attendance photos
-- ============================================================================

-- Before: a manager of any company could read every attendance photo of every company.
-- After: a manager can read only photos of approved members of their own company.
-- Skipped with a notice when the role running this script may not manage storage policies.
DO $$
BEGIN
    DROP POLICY IF EXISTS "Users and managers can view attendance photo" ON storage.objects;
    CREATE POLICY "Users and managers can view attendance photo"
        ON storage.objects FOR SELECT
        USING (
            bucket_id = 'attendance-photos'
            AND (
                auth.uid()::text = (storage.foldername(name))[1]
                OR EXISTS (
                    SELECT 1 FROM public.company_memberships m
                    WHERE m.user_id::text = (storage.foldername(name))[1]
                      AND m.status = 'approved'
                      AND public.is_company_manager(m.company_id)
                )
            )
        );
EXCEPTION
    WHEN insufficient_privilege THEN
        RAISE NOTICE 'Skipped the attendance photo storage policy: not allowed for this role. Apply section 9 from the Supabase SQL editor.';
END
$$;
