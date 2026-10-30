package com.officemitra.app.feature.approvals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*

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
            title = "Company Approvals",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(16.dp)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Pending",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Approved",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "Rejected",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!uiState.currentUser.isAuthorizedApprover) {
                EmptyState(
                    title = "View Only Access",
                    description = "Only company managers and admins can review pending employee approval requests."
                )
            } else if (filteredRequests.isEmpty()) {
                EmptyState(
                    title = "No $filterStatus Approvals",
                    description = "There are no approval requests under $filterStatus status."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredRequests) { request ->
                        ApprovalCard(
                            employeeName = request.employeeName,
                            employeeId = request.employeeId,
                            department = request.department,
                            requestType = request.requestType,
                            requestDate = request.requestDate,
                            details = request.details,
                            status = request.status,
                            onApproveClick = { viewModel.updateApprovalStatus(request.id, true) },
                            onRejectClick = { viewModel.updateApprovalStatus(request.id, false) },
                            isAuthorizedApprover = uiState.currentUser.isAuthorizedApprover,
                            modifier = Modifier.clickable { onNavigateToDetails(request.id) }
                        )
                    }
                }
            }
        }
    }
}
