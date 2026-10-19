# Office Mitra — Smart Workforce Management Application

Office Mitra is a modern, premium, fully interactive native Android application built with Kotlin and Jetpack Compose.

---

## 🛠 Technology Stack
- **Programming Language**: 100% Kotlin
- **UI Framework**: Jetpack Compose
- **Design System**: Material 3 Design System
- **Architecture**: MVVM Architecture
- **Navigation**: Navigation Compose
- **State Management**: Kotlin Coroutines & StateFlow
- **Dependency Injection**: Hilt
- **Preferences**: DataStore
- **Images**: Coil
- **Camera**: CameraX (Preview & Capture UI)
- **Build System**: Gradle Kotlin DSL

---

## 📱 Features Implemented

1. **Splash Screen**: Animated brand entrance and state navigation check.
2. **Onboarding**: 3-page interactive carousel with DataStore completion persistence.
3. **Authentication Workflow**:
   - Welcome Screen with quick options.
   - Login Screen with email/phone format validation & remember me state.
   - Register Screen with real-time field validation, password strength indicator, and match checking.
   - Forgot Password Screen with reset instructions flow.
4. **Company Onboarding**:
   - Join Company Screen accepting demo code `OM-10245` with Acme Tech Corp preview card.
   - Registration Approval Status Screen showing Pending, Approved, and Rejected status options.
5. **Employee Dashboard (Home)**:
   - Live greeting, company info, profile avatar, unread notification counter.
   - Attendance Summary Card with quick Punch In / Punch Out actions.
   - Quick Actions Grid (Attendance, Apply Leave, Salary Slips, Holidays, My Approvals, History).
   - Monthly Overview counters and Recent Activity feed.
6. **Attendance Module**:
   - Attendance tracking with live clock, selfie photo capture preview, and geofence location status.
   - Attendance History with calendar month selector, status filter chips, and attendance details.
   - Attendance Correction Request form.
7. **Leave Management**:
   - Leave balances summary (Casual, Sick, Earned).
   - Status Tabs (Pending, Approved, Rejected, Cancelled).
   - Apply Leave form with date pickers, days calculation, and reason validation.
   - Leave Request details and request cancellation.
8. **My Approvals**:
   - Pending, Approved, and Rejected request tabs.
   - Authorized Approver role switch toggle to test manager approval actions vs normal employee read-only mode.
   - Request detail screen with decision comment input.
9. **Holiday Calendar**:
   - Upcoming and past company holiday listings.
   - Holiday cards with date badge and descriptions.
10. **Salary Slips**:
    - Month-wise salary slip cards.
    - Built-in interactive sample PDF Document Viewer screen with download simulation.
11. **Notifications**:
    - Categorized notifications with unread state management.
    - Mark all as read and clear all notifications options.
    - Tapping notification navigates directly to relevant feature screens.
12. **Profile & Settings**:
    - Profile header with designation and employee ID badge.
    - Personal Information section.
    - Edit Profile screen (locking HR-restricted fields).
    - Change Password form.
    - App Preferences with Light/Dark/System theme selector persisted in DataStore.
    - About Application info.
    - Logout dialog resetting session back to Welcome screen.
13. **Supporting Screens**:
    - No Internet / Network Status UI
    - Access Denied
    - Session Expired
    - Generic Error Screen

---

## 🚀 Build Instructions

### Prerequisites
- JDK 17 or higher
- Android SDK (API 34)

### Building the App
Run the following Gradle commands in root directory:

```bash
# Run unit tests
./gradlew test

# Build debug APK
./gradlew assembleDebug
```

---

## 📌 Demo Mode vs Production Scope

- **Demo State**: All screens observe a shared in-memory `DemoRepository` exposing `StateFlow`.
- **Backend Integration**: This UI phase uses local state. Supabase / Firebase / Production REST API integration is planned for future phases.
