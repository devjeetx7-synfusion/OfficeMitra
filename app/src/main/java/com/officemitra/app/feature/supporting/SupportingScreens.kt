package com.officemitra.app.feature.supporting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons

@Composable
fun NoInternetScreen(
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(title = "Network Status", showBackButton = true, onBackClick = onBackClick)
        EmptyState(
            title = "No Internet Connection",
            description = "Please check your Wi-Fi or mobile data connection and try again.",
            icon = AppIcons.WifiOff,
            actionLabel = "Retry Connection",
            onActionClick = onRetryClick
        )
    }
}

@Composable
fun AccessDeniedScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(title = "Access Restricted", showBackButton = true, onBackClick = onBackClick)
        EmptyState(
            title = "Access Denied",
            description = "You do not have permission to access this module. Contact your HR administrator for authorization.",
            icon = AppIcons.Lock,
            actionLabel = "Go Back",
            onActionClick = onBackClick
        )
    }
}

@Composable
fun SessionExpiredScreen(
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(title = "Session Status")
        EmptyState(
            title = "Session Expired",
            description = "Your session has expired due to inactivity. Please log in again to continue.",
            icon = AppIcons.Timer,
            actionLabel = "Log In Again",
            onActionClick = onNavigateToLogin
        )
    }
}

@Composable
fun GenericErrorScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(title = "Error", showBackButton = true, onBackClick = onBackClick)
        ErrorState(
            message = "An unexpected error occurred while loading this screen.",
            onRetryClick = onBackClick
        )
    }
}
