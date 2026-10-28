-- Office Mitra Supabase Database Migration
-- Initial Schema, Functions, Triggers, and Row Level Security Policies

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================================
-- 1. TABLES DEFINITION
-- ============================================================================

-- PROFILES TABLE
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    full_name TEXT NOT NULL,
    email TEXT,
    phone_number TEXT,
    avatar_path TEXT,
    account_status TEXT NOT NULL DEFAULT 'active',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- COMPANIES TABLE
CREATE TABLE IF NOT EXISTS public.companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    code TEXT UNIQUE NOT NULL,
    address TEXT,
    owner_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    status TEXT NOT NULL DEFAULT 'active',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- COMPANY MEMBERSHIPS TABLE
CREATE TABLE IF NOT EXISTS public.company_memberships (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES public.companies(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    employee_identifier TEXT NOT NULL,
    department TEXT NOT NULL,
    job_title TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'approved', -- 'pending', 'approved', 'rejected', 'revoked'
    role TEXT NOT NULL DEFAULT 'employee', -- 'employee', 'manager', 'hr', 'admin'
    joining_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT unique_company_user UNIQUE(company_id, user_id)
);

-- ATTENDANCE TABLE
CREATE TABLE IF NOT EXISTS public.attendance (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES public.companies(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    punch_in TIMESTAMPTZ,
    punch_out TIMESTAMPTZ,
    status TEXT NOT NULL DEFAULT 'Present', -- 'Present', 'Absent', 'Leave', 'Half Day'
    working_duration TEXT DEFAULT '0h 0m',
    notes TEXT,
    photo_url TEXT,
    location TEXT DEFAULT 'Office HQ',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT unique_user_date UNIQUE(user_id, date)
);

-- LEAVE BALANCES TABLE
CREATE TABLE IF NOT EXISTS public.leave_balances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES public.companies(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    casual_leave_total INT NOT NULL DEFAULT 12,
    casual_leave_used INT NOT NULL DEFAULT 0,
    sick_leave_total INT NOT NULL DEFAULT 10,
    sick_leave_used INT NOT NULL DEFAULT 0,
    earned_leave_total INT NOT NULL DEFAULT 15,
    earned_leave_used INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT unique_company_user_leave_balance UNIQUE(company_id, user_id)
);

-- LEAVE REQUESTS TABLE
CREATE TABLE IF NOT EXISTS public.leave_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES public.companies(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    leave_type TEXT NOT NULL, -- 'Casual Leave', 'Sick Leave', 'Earned Leave'
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    days_count INT NOT NULL DEFAULT 1,
    reason TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'Pending', -- 'Pending', 'Approved', 'Rejected', 'Cancelled'
    reviewer_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    reviewer_comment TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT check_valid_date_range CHECK (end_date >= start_date)
);

-- APPROVAL REQUESTS TABLE
CREATE TABLE IF NOT EXISTS public.approval_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES public.companies(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    request_type TEXT NOT NULL, -- 'Leave Request', 'Attendance Correction', 'Expense Claim'
    request_date DATE NOT NULL DEFAULT CURRENT_DATE,
    details TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'Pending', -- 'Pending', 'Approved', 'Rejected'
    decision_comment TEXT,
    reviewer_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- HOLIDAYS TABLE
CREATE TABLE IF NOT EXISTS public.holidays (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES public.companies(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    date DATE NOT NULL,
    day_of_week TEXT NOT NULL,
    description TEXT,
    month TEXT NOT NULL,
    year TEXT NOT NULL,
    is_upcoming BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- SALARY SLIPS TABLE
CREATE TABLE IF NOT EXISTS public.salary_slips (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES public.companies(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    month_year TEXT NOT NULL,
    period TEXT NOT NULL,
    publish_date DATE NOT NULL,
    basic_salary TEXT NOT NULL,
    net_salary TEXT NOT NULL,
    pdf_path TEXT NOT NULL,
    is_published BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- NOTIFICATIONS TABLE
CREATE TABLE IF NOT EXISTS public.notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    company_id UUID REFERENCES public.companies(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    category TEXT NOT NULL, -- 'Attendance', 'Leave', 'Salary Slip', 'Company Updates', 'Registration Status'
    is_read BOOLEAN NOT NULL DEFAULT false,
    target_route TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- AUDIT LOGS TABLE
CREATE TABLE IF NOT EXISTS public.audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID REFERENCES public.companies(id) ON DELETE SET NULL,
    actor_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    action TEXT NOT NULL,
    entity_type TEXT NOT NULL,
    entity_id TEXT,
    payload JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================================================================
-- 2. INDEXES
-- ============================================================================

CREATE INDEX IF NOT EXISTS idx_company_memberships_user ON public.company_memberships(user_id);
CREATE INDEX IF NOT EXISTS idx_company_memberships_company ON public.company_memberships(company_id);
CREATE INDEX IF NOT EXISTS idx_attendance_user_date ON public.attendance(user_id, date);
CREATE INDEX IF NOT EXISTS idx_leave_requests_user ON public.leave_requests(user_id);
CREATE INDEX IF NOT EXISTS idx_leave_requests_company ON public.leave_requests(company_id);
CREATE INDEX IF NOT EXISTS idx_approval_requests_company ON public.approval_requests(company_id);
CREATE INDEX IF NOT EXISTS idx_salary_slips_user ON public.salary_slips(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON public.notifications(user_id);

-- ============================================================================
-- 3. HELPER FUNCTIONS & TRIGGERS
-- ============================================================================

-- Function: User Profile Provisioning Trigger
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO public.profiles (id, full_name, email, phone_number)
    VALUES (
        NEW.id,
        COALESCE(NEW.raw_user_meta_data->>'full_name', 'Employee'),
        NEW.email,
        COALESCE(NEW.raw_user_meta_data->>'phone_number', '')
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER SET search_path = public;

DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();

-- Non-recursive Helper Functions for RLS
CREATE OR REPLACE FUNCTION public.get_user_company_id(p_user_id UUID)
RETURNS UUID AS $$
    SELECT company_id FROM public.company_memberships
    WHERE user_id = p_user_id AND status = 'approved'
    LIMIT 1;
$$ LANGUAGE sql SECURITY DEFINER SET search_path = public;

CREATE OR REPLACE FUNCTION public.get_user_role(p_user_id UUID)
RETURNS TEXT AS $$
    SELECT role FROM public.company_memberships
    WHERE user_id = p_user_id AND status = 'approved'
    LIMIT 1;
$$ LANGUAGE sql SECURITY DEFINER SET search_path = public;

CREATE OR REPLACE FUNCTION public.is_manager_or_higher(p_user_id UUID)
RETURNS BOOLEAN AS $$
    SELECT EXISTS (
        SELECT 1 FROM public.company_memberships
        WHERE user_id = p_user_id AND status = 'approved' AND role IN ('manager', 'hr', 'admin')
    );
$$ LANGUAGE sql SECURITY DEFINER SET search_path = public;

-- ============================================================================
-- 4. ROW LEVEL SECURITY (RLS) POLICIES
-- ============================================================================

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.companies ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.company_memberships ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.attendance ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.leave_balances ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.leave_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.approval_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.holidays ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.salary_slips ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.audit_logs ENABLE ROW LEVEL SECURITY;

-- PROFILES POLICIES
CREATE POLICY "Users can view their own profile"
    ON public.profiles FOR SELECT
    USING (auth.uid() = id);

CREATE POLICY "Users can update their own profile"
    ON public.profiles FOR UPDATE
    USING (auth.uid() = id);

CREATE POLICY "Company members can view profiles in same company"
    ON public.profiles FOR SELECT
    USING (
        EXISTS (
            SELECT 1 FROM public.company_memberships m1
            JOIN public.company_memberships m2 ON m1.company_id = m2.company_id
            WHERE m1.user_id = auth.uid() AND m2.user_id = public.profiles.id
              AND m1.status = 'approved' AND m2.status = 'approved'
        )
    );

-- COMPANIES POLICIES
CREATE POLICY "Members can view their company"
    ON public.companies FOR SELECT
    USING (
        id IN (
            SELECT company_id FROM public.company_memberships
            WHERE user_id = auth.uid() AND status = 'approved'
        )
    );

CREATE POLICY "Authenticated users can lookup company code"
    ON public.companies FOR SELECT
    USING (auth.role() = 'authenticated');

CREATE POLICY "Authenticated users can create companies"
    ON public.companies FOR INSERT
    WITH CHECK (auth.role() = 'authenticated');

-- COMPANY MEMBERSHIPS POLICIES
CREATE POLICY "Users can view their own membership"
    ON public.company_memberships FOR SELECT
    USING (user_id = auth.uid());

CREATE POLICY "Users can apply to join company"
    ON public.company_memberships FOR INSERT
    WITH CHECK (user_id = auth.uid() AND role = 'employee');

CREATE POLICY "Managers/Admins can view company memberships"
    ON public.company_memberships FOR SELECT
    USING (
        company_id = public.get_user_company_id(auth.uid())
        AND public.is_manager_or_higher(auth.uid())
    );

-- ATTENDANCE POLICIES
CREATE POLICY "Users can view their own attendance"
    ON public.attendance FOR SELECT
    USING (user_id = auth.uid());

CREATE POLICY "Users can punch in/out for themselves"
    ON public.attendance FOR INSERT
    WITH CHECK (user_id = auth.uid());

CREATE POLICY "Users can update their own attendance punch"
    ON public.attendance FOR UPDATE
    USING (user_id = auth.uid());

CREATE POLICY "Managers can view company attendance"
    ON public.attendance FOR SELECT
    USING (
        company_id = public.get_user_company_id(auth.uid())
        AND public.is_manager_or_higher(auth.uid())
    );

-- LEAVE BALANCES POLICIES
CREATE POLICY "Users can view their own leave balances"
    ON public.leave_balances FOR SELECT
    USING (user_id = auth.uid());

-- LEAVE REQUESTS POLICIES
CREATE POLICY "Users can view their own leave requests"
    ON public.leave_requests FOR SELECT
    USING (user_id = auth.uid());

CREATE POLICY "Users can create leave requests for themselves"
    ON public.leave_requests FOR INSERT
    WITH CHECK (user_id = auth.uid());

CREATE POLICY "Users can cancel their own pending leave requests"
    ON public.leave_requests FOR UPDATE
    USING (user_id = auth.uid() AND status = 'Pending');

CREATE POLICY "Managers can view company leave requests"
    ON public.leave_requests FOR SELECT
    USING (
        company_id = public.get_user_company_id(auth.uid())
        AND public.is_manager_or_higher(auth.uid())
    );

CREATE POLICY "Managers can update/review leave requests"
    ON public.leave_requests FOR UPDATE
    USING (
        company_id = public.get_user_company_id(auth.uid())
        AND public.is_manager_or_higher(auth.uid())
    );

-- APPROVAL REQUESTS POLICIES
CREATE POLICY "Users can view their own approval requests"
    ON public.approval_requests FOR SELECT
    USING (user_id = auth.uid());

CREATE POLICY "Users can submit approval requests"
    ON public.approval_requests FOR INSERT
    WITH CHECK (user_id = auth.uid());

CREATE POLICY "Managers can view and review company approval requests"
    ON public.approval_requests FOR ALL
    USING (
        company_id = public.get_user_company_id(auth.uid())
        AND public.is_manager_or_higher(auth.uid())
    );

-- HOLIDAYS POLICIES
CREATE POLICY "Members can view company holidays"
    ON public.holidays FOR SELECT
    USING (
        company_id IN (
            SELECT company_id FROM public.company_memberships
            WHERE user_id = auth.uid() AND status = 'approved'
        )
    );

-- SALARY SLIPS POLICIES
CREATE POLICY "Users can view their own published salary slips"
    ON public.salary_slips FOR SELECT
    USING (user_id = auth.uid() AND is_published = true);

-- NOTIFICATIONS POLICIES
CREATE POLICY "Users can view their own notifications"
    ON public.notifications FOR SELECT
    USING (user_id = auth.uid());

CREATE POLICY "Users can update their own notification read status"
    ON public.notifications FOR UPDATE
    USING (user_id = auth.uid());

-- AUDIT LOGS POLICIES
CREATE POLICY "Admins can view company audit logs"
    ON public.audit_logs FOR SELECT
    USING (
        company_id = public.get_user_company_id(auth.uid())
        AND public.get_user_role(auth.uid()) = 'admin'
    );

-- ============================================================================
-- 5. STORAGE BUCKETS AND STORAGE POLICIES
-- ============================================================================

INSERT INTO storage.buckets (id, name, public)
VALUES ('salary-slips', 'salary-slips', false)
ON CONFLICT (id) DO NOTHING;

INSERT INTO storage.buckets (id, name, public)
VALUES ('avatars', 'avatars', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO storage.buckets (id, name, public)
VALUES ('attendance-photos', 'attendance-photos', false)
ON CONFLICT (id) DO NOTHING;

-- Storage Policies for Salary Slips (Private)
CREATE POLICY "Users can download their own salary slip PDF"
    ON storage.objects FOR SELECT
    USING (
        bucket_id = 'salary-slips'
        AND auth.uid()::text = (storage.foldername(name))[1]
    );

-- Storage Policies for Avatars
CREATE POLICY "Authenticated users can upload avatar"
    ON storage.objects FOR INSERT
    WITH CHECK (
        bucket_id = 'avatars'
        AND auth.uid()::text = (storage.foldername(name))[1]
    );

CREATE POLICY "Anyone can view avatars"
    ON storage.objects FOR SELECT
    USING (bucket_id = 'avatars');

-- Storage Policies for Attendance Photos
CREATE POLICY "Users can upload attendance photo"
    ON storage.objects FOR INSERT
    WITH CHECK (
        bucket_id = 'attendance-photos'
        AND auth.uid()::text = (storage.foldername(name))[1]
    );

CREATE POLICY "Users and managers can view attendance photo"
    ON storage.objects FOR SELECT
    USING (
        bucket_id = 'attendance-photos'
        AND (
            auth.uid()::text = (storage.foldername(name))[1]
            OR public.is_manager_or_higher(auth.uid())
        )
    );
