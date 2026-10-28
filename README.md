# Office Mitra — Smart Workforce Management Application

Office Mitra is a modern, production-grade native Android workforce management application built with Kotlin, Jetpack Compose, and fully integrated with a secure Supabase backend.

---

## 🛠 Technology Stack
- **Programming Language**: 100% Kotlin
- **UI Framework**: Jetpack Compose
- **Design System**: Material 3 Design System
- **Backend Infrastructure**: Supabase (Auth, PostgreSQL Database, Row Level Security, Storage, Realtime)
- **Supabase Kotlin SDK**: `io.github.jan-tennert.supabase` (GoTrue, Postgrest, Storage, Realtime v2.1.0)
- **HTTP Client**: Ktor CIO (`io.ktor:ktor-client-cio`)
- **JSON Serialization**: `kotlinx-serialization-json`
- **Architecture**: MVVM Architecture with Clean Repository Pattern
- **Navigation**: Navigation Compose
- **State Management**: Kotlin Coroutines & StateFlow
- **Dependency Injection**: Hilt
- **Preferences**: DataStore
- **Images**: Coil
- **Camera**: CameraX
- **Build System**: Gradle Kotlin DSL

---

## 🔒 Supabase Architecture & Security Model

Office Mitra uses a secure, direct-to-database Supabase architecture with PostgreSQL Row Level Security (RLS) enforcing access control at the database level.

### 1. Database Schema (`supabase/migrations/00001_initial_schema.sql`)
- **`profiles`**: Linked to `auth.users(id)` with auto-provisioning trigger on signup. Stores user profile metadata (`full_name`, `email`, `phone_number`, `avatar_path`, `account_status`).
- **`companies`**: Company organizational units with unique invitation codes (`code`), name, address, and owner link.
- **`company_memberships`**: Relates users to companies with strict roles (`employee`, `manager`, `hr`, `admin`), employee identifier, department, job title, and approval status (`pending`, `approved`, `rejected`).
- **`attendance`**: Daily attendance punch records (`punch_in`, `punch_out`, `working_duration`, `status`, `photo_url`, `location`). Unique constraint per user/date.
- **`leave_balances`**: Total and used leave entitlements (Casual, Sick, Earned) per employee.
- **`leave_requests`**: Employee leave applications with status tracking (`Pending`, `Approved`, `Rejected`, `Cancelled`) and reviewer comments.
- **`approval_requests`**: Manager approval queue for leave requests, attendance corrections, and expense claims.
- **`holidays`**: Company holiday calendar entries.
- **`salary_slips`**: Confidential employee salary slip records linked to private PDF storage paths.
- **`notifications`**: Targeted user notifications with category, read status, and navigation route targets.
- **`audit_logs`**: System audit trail logging sensitive administrative and approval actions.

### 2. Row Level Security (RLS)
RLS is enabled on **every** table exposed through the Supabase API:
- **User Data Isolation**: Employees can only read/update their own profile, attendance, leave requests, leave balances, salary slips, and notifications.
- **Company Boundary**: Employees can only view data associated with their active approved company membership.
- **Role Control**: Ordinary employees cannot alter their own company role or approve their own leave/attendance correction requests.
- **Manager Authorization**: Managers, HR, and Admins are evaluated using `SECURITY DEFINER` non-recursive helper functions (`public.is_manager_or_higher()` and `public.get_user_company_id()`) to view and act on pending approval requests within their company scope.
- **Private File Storage**:
  - `salary-slips`: Private bucket. Access restricted to the owner matching `auth.uid()`.
  - `attendance-photos`: Private bucket. Access permitted to the photo owner and company managers.
  - `avatars`: Public read, owner-only write.

---

## ⚡ Supabase Setup & Migration Guide

To connect a live Supabase project with Office Mitra:

### 1. Configure Environment Variables
Copy or set the following build properties in `local.properties` or system environment variables:

```properties
SUPABASE_URL=https://your-supabase-project.supabase.co
SUPABASE_PUBLISHABLE_KEY=your-supabase-publishable-anon-key
```

### 2. Run Database Migrations
Apply the migration script to your Supabase PostgreSQL database using the Supabase CLI or SQL Editor in your Supabase Dashboard:

```bash
# Using Supabase CLI
supabase db push
```
Or paste the contents of `supabase/migrations/00001_initial_schema.sql` into the Supabase Dashboard **SQL Editor** and run it.

### 3. Authentication Configuration
In your Supabase Dashboard under **Authentication > Providers**:
1. Enable **Email / Password** authentication.
2. (Optional) Disable email confirmation requirement for instant sign-in, or configure site URL redirect for password recovery flows.

---

## 📱 Integrated Application Features

1. **Supabase Authentication**:
   - Registration with automatic profile provisioning trigger.
   - Email / Password Login with session persistence across app restarts.
   - Password Recovery reset email request flow.
   - Clean Session Sign Out with preference cleanup.
2. **Company Onboarding**:
   - Lookup company by code (`OM-xxxxx`).
   - Apply to join company or create a new company as Admin.
   - View membership approval status.
3. **Real-time Home Dashboard**:
   - Live greeting, active company info, and real notification counters.
   - One-tap Punch In / Punch Out with server timestamp tracking.
   - Pull-to-refresh sync with Supabase Postgrest.
4. **Attendance Tracking & History**:
   - Punch in/out session management.
   - Attendance history query with date filters.
   - Attendance correction submission queueing approval requests.
5. **Leave Management & Approvals**:
   - Live leave balances (Casual, Sick, Earned).
   - Apply for leave with start/end dates and reason.
   - Manager approval dashboard to approve/reject requests with reviewer comments.
6. **Holiday Calendar & Salary Slips**:
   - Company holiday list loaded from database.
   - Confidential salary slips metadata with PDF viewer integration.
7. **In-App Notifications**:
   - User notifications query with read status updates and clear options.
8. **Profile & Settings**:
   - Profile data updates persisted to Supabase.
   - Theme selection (Light/Dark/System) saved in DataStore.

---

## 🚀 Build & Test Instructions

### Prerequisites
- JDK 17 or JDK 21
- Android SDK (API 34)

### Building and Testing
Run the following Gradle commands in the project root:

```bash
# Run unit tests
./gradlew test --no-daemon

# Compile debug sources
./gradlew compileDebugSources --no-daemon

# Build debug APK
./gradlew assembleDebug --no-daemon
```
