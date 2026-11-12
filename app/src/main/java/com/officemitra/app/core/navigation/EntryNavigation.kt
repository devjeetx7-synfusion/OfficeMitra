package com.officemitra.app.core.navigation

import androidx.navigation.NavHostController
import com.officemitra.app.data.session.EntryDestination

fun EntryDestination.toRoute(): String = when (this) {
    EntryDestination.ONBOARDING -> Screen.Onboarding.route
    EntryDestination.WELCOME -> Screen.Welcome.route
    EntryDestination.HOME -> Screen.Home.route
    EntryDestination.JOIN_COMPANY -> Screen.JoinCompany.route
    EntryDestination.REGISTRATION_STATUS -> Screen.RegistrationStatus.route
    EntryDestination.ACCOUNT_SUSPENDED -> Screen.AccountSuspended.route
    EntryDestination.RESET_PASSWORD -> Screen.ResetPassword.route
    EntryDestination.NO_INTERNET -> Screen.NoInternet.route
    EntryDestination.ERROR -> Screen.GenericError.route
}

/** Replaces the whole back stack, so "back" can never return to a screen of the previous session state. */
fun NavHostController.replaceAllWith(route: String) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToEntry(destination: EntryDestination) = replaceAllWith(destination.toRoute())

/** Goes back, or to [fallbackRoute] when this is the only screen, instead of leaving an empty window. */
fun NavHostController.backOr(fallbackRoute: String) {
    if (previousBackStackEntry != null) popBackStack() else replaceAllWith(fallbackRoute)
}
