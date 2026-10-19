package com.officemitra.app.feature.approvals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.ErrorColor
import com.officemitra.app.core.theme.SuccessColor
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun ApprovalDetailsScreen(
    approvalId: String,
    onBackClick: () -> Unit,
    viewModel: ApprovalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val request = uiState.approvalRequests.find { it.id == approvalId } ?: uiState.approvalRequests.firstOrNull()
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Approval Details",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            request?.let { req ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = req.employeeName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            StatusBadge(status = req.status)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Type: ${req.requestType}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(text = "Date: ${req.requestDate}", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Details:", style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                        Text(text = req.details, style = MaterialTheme.typography.bodyMedium)

                        if (req.decisionComment != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Decision Comment:", style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                            Text(text = req.decisionComment, style = MaterialTheme.typography.bodyMedium)
                        } else if (uiState.currentUser.isAuthorizedApprover && req.status.equals("Pending", ignoreCase = true)) {
                            Spacer(modifier = Modifier.height(16.dp))
                            AppTextField(
                                value = comment,
                                onValueChange = { comment = it },
                                label = "Add Approver Comment",
                                placeholder = "Enter reason or comments..."
                            )
                        }
                    }
                }
            } ?: EmptyState(title = "Request Not Found", description = "No request found with ID: $approvalId")

            if (uiState.currentUser.isAuthorizedApprover && request?.status.equals("Pending", ignoreCase = true)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            request?.let { viewModel.updateApprovalStatus(it.id, true, comment) }
                            onBackClick()
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessColor)
                    ) {
                        Text("Approve")
                    }
                    Button(
                        onClick = {
                            request?.let { viewModel.updateApprovalStatus(it.id, false, comment) }
                            onBackClick()
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorColor)
                    ) {
                        Text("Reject")
                    }
                }
            } else {
                PrimaryButton(text = "Back", onClick = onBackClick)
            }
        }
    }
}
