package com.officemitra.app.core.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")

    /** Opened only from a password-reset email link. */
    object ResetPassword : Screen("reset_password")
    object JoinCompany : Screen("join_company")
    object RegistrationStatus : Screen("registration_status")

    // Main Bottom Navigation Tabs
    object Home : Screen("home")
    object Attendance : Screen("attendance")
    object Leave : Screen("leave")
    object Profile : Screen("profile")

    // Secondary Feature Screens
    object AttendanceHistory : Screen("attendance_history")
    object AttendanceCorrection : Screen("attendance_correction")
    object ApplyLeave : Screen("apply_leave")
    object LeaveDetails : Screen("leave_details/{leaveId}") {
        fun createRoute(leaveId: String) = "leave_details/$leaveId"
    }
    object Approvals : Screen("approvals")
    object ApprovalDetails : Screen("approval_details/{approvalId}") {
        fun createRoute(approvalId: String) = "approval_details/$approvalId"
    }
    object Holidays : Screen("holidays")
    object SalarySlips : Screen("salary_slips")
    object SalaryPdfViewer : Screen("salary_pdf_viewer/{salaryId}") {
        fun createRoute(salaryId: String) = "salary_pdf_viewer/$salaryId"
    }
    object Notifications : Screen("notifications")
    object EditProfile : Screen("edit_profile")
    object ChangePassword : Screen("change_password")
    object AppSettings : Screen("app_settings")
    object AboutApp : Screen("about_app")

    // Supporting Error / Utility Screens
    object AccessDenied : Screen("access_denied")
    object AccountSuspended : Screen("account_suspended")
    object SessionExpired : Screen("session_expired")
    object NoInternet : Screen("no_internet")
    object GenericError : Screen("generic_error")
}

/**
 * Decides which routes need a signed-in user.
 *
 * The list names the routes that are public; every other route, including any added later, is
 * protected by default.
 */
object RouteAccess {
    private val publicRoutes: Set<String> by lazy {
        setOf(
            Screen.Splash.route,
            Screen.Onboarding.route,
            Screen.Welcome.route,
            Screen.Login.route,
            Screen.Register.route,
            Screen.ForgotPassword.route,
            Screen.AboutApp.route,
            Screen.SessionExpired.route,
            Screen.NoInternet.route,
            Screen.GenericError.route
        )
    }

    fun requiresSession(route: String?): Boolean = route != null && route !in publicRoutes
}
