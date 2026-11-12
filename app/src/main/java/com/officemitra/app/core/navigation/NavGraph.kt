package com.officemitra.app.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.officemitra.app.feature.approvals.ApprovalDetailsScreen
import com.officemitra.app.feature.approvals.ApprovalsScreen
import com.officemitra.app.feature.attendance.AttendanceCorrectionScreen
import com.officemitra.app.feature.attendance.AttendanceHistoryScreen
import com.officemitra.app.feature.attendance.AttendanceScreen
import com.officemitra.app.feature.auth.*
import com.officemitra.app.feature.company.JoinCompanyScreen
import com.officemitra.app.feature.company.RegistrationStatusScreen
import com.officemitra.app.feature.holidays.HolidaysScreen
import com.officemitra.app.feature.home.HomeScreen
import com.officemitra.app.feature.leave.ApplyLeaveScreen
import com.officemitra.app.feature.leave.LeaveDetailsScreen
import com.officemitra.app.feature.leave.LeaveScreen
import com.officemitra.app.feature.notifications.NotificationsScreen
import com.officemitra.app.feature.onboarding.OnboardingScreen
import com.officemitra.app.feature.profile.*
import com.officemitra.app.feature.salary.SalaryPdfViewerScreen
import com.officemitra.app.feature.salary.SalarySlipsScreen
import com.officemitra.app.feature.splash.SplashScreen
import com.officemitra.app.feature.supporting.AccessDeniedScreen
import com.officemitra.app.feature.supporting.GenericErrorScreen
import com.officemitra.app.feature.supporting.NoInternetScreen
import com.officemitra.app.feature.supporting.SessionExpiredScreen

/**
 * @param onSignOut ends the session; the route guard in `MainScreen` then leaves the protected area.
 * @param onRetryEntry re-evaluates the session and account and navigates to where the user belongs.
 * @param isSessionBusy true while one of the two actions above is running.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {},
    onRetryEntry: () -> Unit = {},
    isSessionBusy: Boolean = false
) {
    val activity = LocalContext.current as? Activity

    // When a screen is the only one on the stack, popping it would leave an empty window.
    // "Back" then does what the system back button does there: it leaves the app.
    val backOrExit: () -> Unit = {
        if (navController.previousBackStackEntry != null) navController.popBackStack() else activity?.finish()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // Splash: the destination is decided from the restored session by SessionViewModel.
        composable(Screen.Splash.route) {
            SplashScreen()
        }

        // Onboarding
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Welcome
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) { launchSingleTop = true } },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) { launchSingleTop = true } },
                // Joining a company needs an account. After registration or login, a user without a
                // company is taken to the join screen automatically.
                onNavigateToJoinCompany = { navController.navigate(Screen.Register.route) { launchSingleTop = true } },
                onNavigateToTerms = { navController.navigate(Screen.AboutApp.route) }
            )
        }

        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                onAuthenticated = { destination -> navController.navigateToEntry(destination) },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Welcome.route)
                        launchSingleTop = true
                    }
                },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) { launchSingleTop = true } },
                onBackClick = { navController.backOr(Screen.Welcome.route) }
            )
        }

        // Register
        composable(Screen.Register.route) {
            RegisterScreen(
                onAuthenticated = { destination -> navController.navigateToEntry(destination) },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route)
                        launchSingleTop = true
                    }
                },
                onBackClick = { navController.backOr(Screen.Welcome.route) }
            )
        }

        // Forgot Password
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.backOr(Screen.Welcome.route) }
            )
        }

        // Reset Password: reachable only through a password-reset email link.
        composable(Screen.ResetPassword.route) {
            ChangePasswordScreen(
                onBackClick = onRetryEntry,
                isRecovery = true
            )
        }

        // Join Company
        composable(Screen.JoinCompany.route) {
            JoinCompanyScreen(
                onNavigateToStatus = { navController.replaceAllWith(Screen.RegistrationStatus.route) },
                onBackClick = backOrExit,
                onLogout = onSignOut
            )
        }

        // Registration Status
        composable(Screen.RegistrationStatus.route) {
            RegistrationStatusScreen(
                onNavigateToHome = { navController.replaceAllWith(Screen.Home.route) },
                onBackClick = backOrExit,
                onLogout = onSignOut,
                onNavigateToJoinCompany = { navController.replaceAllWith(Screen.JoinCompany.route) }
            )
        }

        // Main Dashboard Tabs
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAttendance = { navController.navigate(Screen.Attendance.route) },
                onNavigateToApplyLeave = { navController.navigate(Screen.ApplyLeave.route) },
                onNavigateToSalarySlips = { navController.navigate(Screen.SalarySlips.route) },
                onNavigateToHolidays = { navController.navigate(Screen.Holidays.route) },
                onNavigateToApprovals = { navController.navigate(Screen.Approvals.route) },
                onNavigateToAttendanceHistory = { navController.navigate(Screen.AttendanceHistory.route) },
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(Screen.Attendance.route) {
            AttendanceScreen(
                onNavigateToHistory = { navController.navigate(Screen.AttendanceHistory.route) },
                onNavigateToCorrection = { navController.navigate(Screen.AttendanceCorrection.route) }
            )
        }

        composable(Screen.Leave.route) {
            LeaveScreen(
                onNavigateToApplyLeave = { navController.navigate(Screen.ApplyLeave.route) },
                onNavigateToDetails = { leaveId -> navController.navigate(Screen.LeaveDetails.createRoute(leaveId)) }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                onNavigateToChangePassword = { navController.navigate(Screen.ChangePassword.route) },
                onNavigateToAppSettings = { navController.navigate(Screen.AppSettings.route) },
                onNavigateToAboutApp = { navController.navigate(Screen.AboutApp.route) },
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                // Kept for the screen's signature. Leaving the app after logout is done by the
                // route guard in MainScreen, which reacts to the session itself.
                onLogout = {}
            )
        }

        // Secondary Feature Routes
        composable(Screen.AttendanceHistory.route) {
            AttendanceHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToCorrection = { navController.navigate(Screen.AttendanceCorrection.route) }
            )
        }

        composable(Screen.AttendanceCorrection.route) {
            AttendanceCorrectionScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ApplyLeave.route) {
            ApplyLeaveScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.LeaveDetails.route,
            arguments = listOf(navArgument("leaveId") { type = NavType.StringType })
        ) { backStackEntry ->
            val leaveId = backStackEntry.arguments?.getString("leaveId") ?: ""
            LeaveDetailsScreen(
                leaveId = leaveId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Approvals.route) {
            ApprovalsScreen(
                onNavigateToDetails = { approvalId -> navController.navigate(Screen.ApprovalDetails.createRoute(approvalId)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ApprovalDetails.route,
            arguments = listOf(navArgument("approvalId") { type = NavType.StringType })
        ) { backStackEntry ->
            val approvalId = backStackEntry.arguments?.getString("approvalId") ?: ""
            ApprovalDetailsScreen(
                approvalId = approvalId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Holidays.route) {
            HolidaysScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SalarySlips.route) {
            SalarySlipsScreen(
                onNavigateToPdfViewer = { salaryId -> navController.navigate(Screen.SalaryPdfViewer.createRoute(salaryId)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SalaryPdfViewer.route,
            arguments = listOf(navArgument("salaryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val salaryId = backStackEntry.arguments?.getString("salaryId") ?: ""
            SalaryPdfViewerScreen(
                salaryId = salaryId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onNavigateToTarget = { target ->
                    when (target) {
                        "leave" -> navController.navigate(Screen.Leave.route)
                        "salary" -> navController.navigate(Screen.SalarySlips.route)
                        "holidays" -> navController.navigate(Screen.Holidays.route)
                        "attendance" -> navController.navigate(Screen.Attendance.route)
                        else -> {}
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AppSettings.route) {
            AppSettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AboutApp.route) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Supporting Screens
        composable(Screen.AccessDenied.route) {
            AccessDeniedScreen(onBackClick = { navController.backOr(Screen.Home.route) })
        }

        composable(Screen.AccountSuspended.route) {
            AccessDeniedScreen(
                onBackClick = onSignOut,
                title = "Account Disabled",
                description = "Your account has been disabled. Please contact your HR administrator.",
                actionLabel = "Log Out",
                showBackButton = false
            )
        }

        composable(Screen.SessionExpired.route) {
            SessionExpiredScreen(onNavigateToLogin = {
                // Keep Welcome underneath so that "back" from the login form has somewhere to go.
                navController.replaceAllWith(Screen.Welcome.route)
                navController.navigate(Screen.Login.route) { launchSingleTop = true }
            })
        }

        composable(Screen.NoInternet.route) {
            NoInternetScreen(
                onRetryClick = onRetryEntry,
                onBackClick = backOrExit,
                isRetrying = isSessionBusy
            )
        }

        composable(Screen.GenericError.route) {
            GenericErrorScreen(
                onBackClick = backOrExit,
                onRetryClick = onRetryEntry
            )
        }
    }
}
