package com.officemitra.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
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
import com.officemitra.app.core.navigation.AppNavGraph
import com.officemitra.app.core.navigation.Screen
import com.officemitra.app.core.theme.AppIcons

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
            modifier = Modifier.fillMaxSize()
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
}
