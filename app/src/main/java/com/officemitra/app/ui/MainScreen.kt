package com.officemitra.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.officemitra.app.core.components.BottomNavItem
import com.officemitra.app.core.components.FloatingNavigationDock
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.navigation.AppNavGraph
import com.officemitra.app.core.navigation.RouteAccess
import com.officemitra.app.core.navigation.Screen
import com.officemitra.app.core.navigation.navigateToEntry
import com.officemitra.app.core.navigation.replaceAllWith
import com.officemitra.app.core.theme.AppIcons
import com.officemitra.app.data.repository.AuthState
import com.officemitra.app.data.session.EntryDestination
import com.officemitra.app.feature.session.SessionViewModel

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authState by sessionViewModel.authState.collectAsState()
    val sessionNotice by sessionViewModel.notice.collectAsState()
    val isSessionBusy by sessionViewModel.isBusy.collectAsState()

    // Session-driven navigation: app start, email links and "retry".
    LaunchedEffect(navController) {
        sessionViewModel.navigation.collect { request ->
            val current = navController.currentDestination?.route
            // After the system restored a screen inside the app, an unchanged "you belong in the
            // app" decision must not reset the user to the dashboard.
            val keepRestoredScreen = request.isStartup &&
                request.destination == EntryDestination.HOME &&
                current != null &&
                current != Screen.Splash.route &&
                RouteAccess.requiresSession(current)
            if (!keepRestoredScreen) navController.navigateToEntry(request.destination)
        }
    }

    // Route guard: a protected screen is never left on display once the session is gone, whatever
    // ended it (logout, expiry, revoked refresh token).
    LaunchedEffect(authState, currentRoute) {
        if (authState is AuthState.SignedOut && RouteAccess.requiresSession(currentRoute)) {
            val target = if (sessionViewModel.consumeSessionExpired()) {
                Screen.SessionExpired.route
            } else {
                Screen.Welcome.route
            }
            navController.replaceAllWith(target)
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            title = "Home",
            selectedIcon = AppIcons.Home,
            unselectedIcon = AppIcons.Home
        ),
        BottomNavItem(
            route = Screen.Attendance.route,
            title = "Attendance",
            selectedIcon = AppIcons.Attendance,
            unselectedIcon = AppIcons.Attendance
        ),
        BottomNavItem(
            route = Screen.Leave.route,
            title = "Leave",
            selectedIcon = AppIcons.Leave,
            unselectedIcon = AppIcons.Leave
        ),
        BottomNavItem(
            route = Screen.Profile.route,
            title = "Profile",
            selectedIcon = AppIcons.Profile,
            unselectedIcon = AppIcons.Profile
        )
    )

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        AppNavGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            onSignOut = sessionViewModel::signOut,
            onRetryEntry = sessionViewModel::retryEntry,
            isSessionBusy = isSessionBusy
        )

        AnimatedVisibility(
            visible = showBottomBar,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 280)
            ) + fadeIn(animationSpec = tween(durationMillis = 280)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 220)
            ) + fadeOut(animationSpec = tween(durationMillis = 220)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 12.dp)
        ) {
            FloatingNavigationDock(
                items = bottomNavItems,
                currentRoute = currentRoute,
                onItemClick = { item ->
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }

    sessionNotice?.let { message ->
        AlertDialog(
            onDismissRequest = sessionViewModel::dismissNotice,
            confirmButton = {
                TextButton(onClick = sessionViewModel::dismissNotice) { Text("OK") }
            },
            text = { Text(message) }
        )
    }
}
