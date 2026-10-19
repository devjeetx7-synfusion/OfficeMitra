package com.officemitra.app.feature.leave

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

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
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "Apply Leave") },
                text = { Text("Apply Leave", fontWeight = FontWeight.Bold) },
                containerColor = PrimaryLight,
                contentColor = androidx.compose.ui.graphics.Color.White
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.padding( horizontal = 16.dp, vertical = 12.dp)) {
                // Summary Balances Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Leave Balances (2024)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Total", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                                Text(text = "${uiState.balance.totalLeave} Days", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Used", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                                Text(text = "${uiState.balance.totalUsed} Days", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(text = "Remaining", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                                Text(text = "${uiState.balance.totalRemaining} Days", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = PrimaryLight)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Leave Categories Quick Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Casual", style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                            Text(text = "${uiState.balance.casualLeaveTotal - uiState.balance.casualLeaveUsed} left", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Sick", style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                            Text(text = "${uiState.balance.sickLeaveTotal - uiState.balance.sickLeaveUsed} left", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Earned", style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                            Text(text = "${uiState.balance.earnedLeaveTotal - uiState.balance.earnedLeaveUsed} left", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Pending") })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Approved") })
                    Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Rejected") })
                    Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Cancelled") })
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (filteredRequests.isEmpty()) {
                    EmptyState(
                        title = "No $filterStatus Requests",
                        description = "There are currently no leave requests under this category.",
                        actionLabel = "Apply Leave Now",
                        onActionClick = onNavigateToApplyLeave
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
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
