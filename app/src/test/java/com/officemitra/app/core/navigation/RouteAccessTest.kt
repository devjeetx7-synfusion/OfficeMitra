package com.officemitra.app.core.navigation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RouteAccessTest {

    @Test
    fun `screens that show account data need a session`() {
        listOf(
            Screen.Home,
            Screen.Attendance,
            Screen.Leave,
            Screen.Approvals,
            Screen.Profile,
            Screen.EditProfile,
            Screen.ChangePassword,
            Screen.ResetPassword,
            Screen.JoinCompany,
            Screen.RegistrationStatus,
            Screen.AccountSuspended,
            Screen.SalarySlips,
            Screen.Notifications
        ).forEach { screen ->
            assertTrue("${screen.route} must be protected", RouteAccess.requiresSession(screen.route))
        }
    }

    @Test
    fun `sign-in screens are reachable without a session`() {
        listOf(
            Screen.Splash,
            Screen.Onboarding,
            Screen.Welcome,
            Screen.Login,
            Screen.Register,
            Screen.ForgotPassword,
            Screen.SessionExpired,
            Screen.NoInternet
        ).forEach { screen ->
            assertFalse("${screen.route} must be public", RouteAccess.requiresSession(screen.route))
        }
    }

    @Test
    fun `a route that is not listed is protected by default`() {
        assertTrue(RouteAccess.requiresSession("a_screen_added_later"))
        assertTrue(RouteAccess.requiresSession("leave_details/{leaveId}"))
        // No destination yet (the graph is still being built): nothing to protect.
        assertFalse(RouteAccess.requiresSession(null))
    }
}
