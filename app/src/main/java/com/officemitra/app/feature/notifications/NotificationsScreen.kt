package com.officemitra.app.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons

@Composable
fun NotificationsScreen(
    onNavigateToTarget: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Attendance", "Leave", "Salary Slip", "Company Updates")

    val filteredList = notifications.filter {
        selectedCategory == "All" || it.category.equals(selectedCategory, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Notifications",
            showBackButton = true,
            onBackClick = onBackClick,
            actions = {
                IconButton(
                    onClick = viewModel::markAllAsRead,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.DoneAll,
                        contentDescription = "Mark all as read",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            FilterChipRow(
                options = categories,
                selectedOption = selectedCategory,
                onOptionSelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredList.isEmpty()) {
                EmptyState(
                    title = "No Notifications",
                    description = "You have no notifications under the '$selectedCategory' category."
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { showClearConfirmDialog = true },
                        modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                    ) {
                        Text(
                            text = "Clear All",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { notification ->
                        NotificationCard(
                            title = notification.title,
                            message = notification.message,
                            timestamp = notification.timestamp,
                            isRead = notification.isRead,
                            category = notification.category,
                            onClick = {
                                viewModel.markAsRead(notification.id)
                                notification.targetRoute?.let { route -> onNavigateToTarget(route) }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showClearConfirmDialog) {
        ConfirmationDialog(
            title = "Clear All Notifications",
            message = "Are you sure you want to clear all notifications?",
            confirmText = "Clear All",
            onConfirm = {
                viewModel.clearAll()
                showClearConfirmDialog = false
            },
            onDismiss = { showClearConfirmDialog = false },
            isDanger = true
        )
    }
}
