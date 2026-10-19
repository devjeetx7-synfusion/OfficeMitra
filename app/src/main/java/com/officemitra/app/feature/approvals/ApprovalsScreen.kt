package com.officemitra.app.feature.approvals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.ErrorColor
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.SuccessColor
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun ApprovalsScreen(
    onNavigateToDetails: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ApprovalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Pending, 1: Approved, 2: Rejected

    val filterStatus = when (selectedTab) {
        0 -> "Pending"
        1 -> "Approved"
        else -> "Rejected"
    }

    val filteredRequests = uiState.approvalRequests.filter { it.status.equals(filterStatus, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "My Approvals",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Approver Role Permission Toggle Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Manager Approver Mode", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (uiState.currentUser.isAuthorizedApprover) "Authorized to approve requests" else "Normal Employee (Read-only view)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryLight
                        )
                    }
                    Switch(
                        checked = uiState.currentUser.isAuthorizedApprover,
                        onCheckedChange = viewModel::toggleApproverRole,
                        colors = SwitchDefaults.colors(checkedThumbColor = PrimaryLight)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Pending") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Approved") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Rejected") })
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredRequests.isEmpty()) {
                EmptyState(
                    title = "No $filterStatus Approvals",
                    description = "There are no pending approval requests in this category."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRequests) { request ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetails(request.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        EmployeeAvatar(imageUrl = null, name = request.employeeName, size = 36)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(text = request.employeeName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            Text(text = "${request.department} | ${request.employeeId}", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                                        }
                                    }
                                    StatusBadge(status = request.status)
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = request.requestType, style = MaterialTheme.typography.labelMedium, color = PrimaryLight)
                                Text(text = request.details, style = MaterialTheme.typography.bodyMedium)

                                // Approver Action Buttons
                                if (uiState.currentUser.isAuthorizedApprover && request.status.equals("Pending", ignoreCase = true)) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.updateApprovalStatus(request.id, true) },
                                            modifier = Modifier.weight(1f).height(38.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = SuccessColor),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Approve", style = MaterialTheme.typography.labelMedium)
                                        }
                                        OutlinedButton(
                                            onClick = { viewModel.updateApprovalStatus(request.id, false) },
                                            modifier = Modifier.weight(1f).height(38.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Reject", style = MaterialTheme.typography.labelMedium, color = ErrorColor)
                                        }
                                    }
                                } else if (!uiState.currentUser.isAuthorizedApprover && request.status.equals("Pending", ignoreCase = true)) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Approval permission required to act on this request.", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
