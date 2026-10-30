package com.officemitra.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
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
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToJoinCompany = { navController.navigate(Screen.JoinCompany.route) },
                onNavigateToTerms = { navController.navigate(Screen.AboutApp.route) }
            )
        }

        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Register
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Forgot Password
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Join Company
        composable(Screen.JoinCompany.route) {
            JoinCompanyScreen(
                onNavigateToStatus = { navController.navigate(Screen.RegistrationStatus.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Registration Status
        composable(Screen.RegistrationStatus.route) {
            RegistrationStatusScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
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
                onLogout = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
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
            AccessDeniedScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.SessionExpired.route) {
            SessionExpiredScreen(onNavigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        composable(Screen.NoInternet.route) {
            NoInternetScreen(onRetryClick = { navController.popBackStack() }, onBackClick = { navController.popBackStack() })
        }

        composable(Screen.GenericError.route) {
            GenericErrorScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
