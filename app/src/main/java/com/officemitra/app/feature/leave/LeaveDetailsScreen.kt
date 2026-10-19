package com.officemitra.app.feature.leave

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun LeaveDetailsScreen(
    leaveId: String,
    onBackClick: () -> Unit,
    viewModel: LeaveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val request = uiState.requests.find { it.id == leaveId } ?: uiState.requests.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Leave Request Details",
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
                            Text(text = req.leaveType, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            StatusBadge(status = req.status)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Duration: ${req.startDate} to ${req.endDate} (${req.daysCount} Days)", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Applied On: ${req.appliedDate}", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Reason:", style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                        Text(text = req.reason, style = MaterialTheme.typography.bodyMedium)

                        req.approverComment?.let { comment ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Approver Comment:", style = MaterialTheme.typography.labelMedium, color = TextSecondaryLight)
                            Text(text = comment, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            } ?: EmptyState(title = "Request Not Found", description = "No leave request found with ID: $leaveId")

            PrimaryButton(text = "Back to Leaves", onClick = onBackClick)
        }
    }
}
