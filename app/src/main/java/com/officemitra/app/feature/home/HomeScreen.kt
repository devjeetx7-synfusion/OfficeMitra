package com.officemitra.app.feature.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
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
import com.officemitra.app.core.theme.*

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
        // Top Header Surface
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
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
                        size = 46
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Good day, ${uiState.user.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = uiState.user.companyName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Attendance Hero Card
            AttendanceStatusCard(
                date = uiState.todayAttendance.date,
                status = uiState.todayAttendance.status,
                punchIn = uiState.todayAttendance.punchIn,
                punchOut = uiState.todayAttendance.punchOut,
                workingDuration = uiState.todayAttendance.workingDuration,
                onPunchClick = viewModel::togglePunchStatus,
                isPunchedIn = uiState.isPunchedIn
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Actions Section
            SectionHeader(title = "Quick Actions")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionTile(
                    title = "Attendance",
                    icon = Icons.Outlined.AccessTime,
                    onClick = onNavigateToAttendance,
                    iconBgColor = PastelCyan,
                    iconDarkBgColor = PastelCyanDark,
                    iconTint = CyanLight,
                    modifier = Modifier.weight(1f)
                )
                QuickActionTile(
                    title = "Apply Leave",
                    icon = Icons.AutoMirrored.Outlined.EventNote,
                    onClick = onNavigateToApplyLeave,
                    iconBgColor = PastelPink,
                    iconDarkBgColor = PastelPinkDark,
                    iconTint = PinkLight,
                    modifier = Modifier.weight(1f)
                )
                QuickActionTile(
                    title = "Salary Slips",
                    icon = Icons.AutoMirrored.Outlined.ReceiptLong,
                    onClick = onNavigateToSalarySlips,
                    iconBgColor = PastelViolet,
                    iconDarkBgColor = PastelVioletDark,
                    iconTint = PrimaryLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionTile(
                    title = "Holidays",
                    icon = Icons.Outlined.BeachAccess,
                    onClick = onNavigateToHolidays,
                    iconBgColor = PastelAmber,
                    iconDarkBgColor = PastelAmberDark,
                    iconTint = WarningLight,
                    modifier = Modifier.weight(1f)
                )
                QuickActionTile(
                    title = "Approvals",
                    icon = Icons.Outlined.HowToReg,
                    onClick = onNavigateToApprovals,
                    iconBgColor = PastelGreen,
                    iconDarkBgColor = PastelGreenDark,
                    iconTint = SuccessLight,
                    modifier = Modifier.weight(1f)
                )
                QuickActionTile(
                    title = "History",
                    icon = Icons.Outlined.History,
                    onClick = onNavigateToAttendanceHistory,
                    iconBgColor = PastelBlue,
                    iconDarkBgColor = PastelBlueDark,
                    iconTint = SecondaryLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Monthly Overview Metrics
            SectionHeader(title = "Monthly Overview")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryMetricCard(
                    title = "Present Days",
                    value = "18 Days",
                    subtitle = "On Track",
                    icon = Icons.Outlined.CheckCircle,
                    accentColor = SuccessLight,
                    modifier = Modifier.weight(1f)
                )
                SummaryMetricCard(
                    title = "Leave Days",
                    value = "2 Days",
                    subtitle = "1 Pending",
                    icon = Icons.Outlined.Event,
                    accentColor = PinkLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Recent Activity Section
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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
