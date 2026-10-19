package com.officemitra.app.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAttendance: () -> Unit,
    onNavigateToApplyLeave: () -> Unit,
    onNavigateToSalarySlips: () -> Unit,
    onNavigateToHolidays: () -> Unit,
    onNavigateToApprovals: () -> Unit,
    onNavigateToAttendanceHistory: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToProfile() }
                ) {
                    EmployeeAvatar(
                        imageUrl = uiState.user.avatarUrl,
                        name = uiState.user.name,
                        size = 48
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Good day, ${uiState.user.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = uiState.user.companyName,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryLight
                        )
                    }
                }

                Box {
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (uiState.unreadNotificationCount > 0) {
                        Badge(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 4.dp, end = 4.dp),
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        ) {
                            Text(text = uiState.unreadNotificationCount.toString())
                        }
                    }
                }
            }
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Attendance Summary Card
            AttendanceCard(
                date = uiState.todayAttendance.date,
                status = uiState.todayAttendance.status,
                punchIn = uiState.todayAttendance.punchIn,
                punchOut = uiState.todayAttendance.punchOut,
                workingDuration = uiState.todayAttendance.workingDuration,
                onPunchClick = viewModel::togglePunchStatus,
                isPunchedIn = uiState.isPunchedIn
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions Section
            SectionHeader(title = "Quick Actions")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Attendance",
                    icon = Icons.Outlined.AccessTime,
                    onClick = onNavigateToAttendance,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = "Apply Leave",
                    icon = Icons.Outlined.EventNote,
                    onClick = onNavigateToApplyLeave,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = "Salary Slips",
                    icon = Icons.Outlined.ReceiptLong,
                    onClick = onNavigateToSalarySlips,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    title = "Holidays",
                    icon = Icons.Outlined.BeachAccess,
                    onClick = onNavigateToHolidays,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = "Approvals",
                    icon = Icons.Outlined.HowToReg,
                    onClick = onNavigateToApprovals,
                    modifier = Modifier.weight(1f)
                )
                QuickActionCard(
                    title = "History",
                    icon = Icons.Outlined.History,
                    onClick = onNavigateToAttendanceHistory,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Monthly Overview
            SectionHeader(title = "Monthly Overview (May 2024)")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    title = "Present",
                    value = "18 Days",
                    subtitle = "+2 Days vs Apr",
                    icon = Icons.Outlined.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Leaves",
                    value = "2 Days",
                    subtitle = "1 Pending",
                    icon = Icons.Outlined.Event,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Activity
            SectionHeader(
                title = "Recent Activity",
                actionText = "View All",
                onActionClick = onNavigateToNotifications
            )

            if (uiState.recentActivities.isEmpty()) {
                EmptyState(
                    title = "No Recent Activity",
                    description = "Your recent notifications and updates will appear here."
                )
            } else {
                uiState.recentActivities.forEach { activity ->
                    NotificationCard(
                        title = activity.title,
                        message = activity.message,
                        timestamp = activity.timestamp,
                        isRead = activity.isRead,
                        category = activity.category,
                        onClick = onNavigateToNotifications,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
