package com.officemitra.app.feature.leave

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons
import com.officemitra.app.core.theme.BrandTeal

@Composable
fun LeaveScreen(
    onNavigateToApplyLeave: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    viewModel: LeaveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Pending, 1: Approved, 2: Rejected, 3: Cancelled

    val filterStatus = when (selectedTab) {
        0 -> "Pending"
        1 -> "Approved"
        2 -> "Rejected"
        else -> "Cancelled"
    }

    val filteredRequests = uiState.requests.filter { it.status.equals(filterStatus, ignoreCase = true) }

    Scaffold(
        topBar = {
            OfficeMitraTopBar(title = "Leave Management")
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToApplyLeave,
                icon = {
                    Icon(
                        imageVector = AppIcons.Add,
                        contentDescription = "Apply Leave",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = { Text("Apply Leave", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold) },
                containerColor = BrandTeal,
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Solid Midnight Ink Leave Balance Hero Card
                LeaveBalanceCard(
                    totalLeave = uiState.balance.totalLeave,
                    usedLeave = uiState.balance.totalUsed,
                    remainingLeave = uiState.balance.totalRemaining,
                    casualRemaining = uiState.balance.casualLeaveTotal - uiState.balance.casualLeaveUsed,
                    sickRemaining = uiState.balance.sickLeaveTotal - uiState.balance.sickLeaveUsed,
                    earnedRemaining = uiState.balance.earnedLeaveTotal - uiState.balance.earnedLeaveUsed
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Single-Line Tabs (Prevents Text Wrapping)
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    edgePadding = 0.dp
                ) {
                    val tabs = listOf("Pending", "Approved", "Rejected", "Cancelled")
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 1,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (filteredRequests.isEmpty()) {
                    EmptyState(
                        title = "No $filterStatus Requests",
                        description = "There are currently no leave requests under $filterStatus status.",
                        actionLabel = "Apply Leave Now",
                        onActionClick = onNavigateToApplyLeave
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(filteredRequests) { request ->
                            Box(modifier = Modifier.clickable { onNavigateToDetails(request.id) }) {
                                LeaveRequestCard(
                                    leaveType = request.leaveType,
                                    startDate = request.startDate,
                                    endDate = request.endDate,
                                    daysCount = request.daysCount,
                                    reason = request.reason,
                                    status = request.status,
                                    onCancelClick = { viewModel.cancelLeave(request.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
