package com.officemitra.app.feature.leave

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*

@Composable
fun ApplyLeaveScreen(
    onBackClick: () -> Unit,
    viewModel: LeaveViewModel = hiltViewModel()
) {
    val leaveTypes = listOf("Casual Leave", "Sick Leave", "Earned Leave")
    var selectedType by remember { mutableStateOf(leaveTypes[0]) }
    var startDate by remember { mutableStateOf("15 May") }
    var endDate by remember { mutableStateOf("16 May") }
    var daysCount by remember { mutableIntStateOf(2) }
    var reason by remember { mutableStateOf("") }
    var reasonError by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Apply Leave",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "New Leave Request",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Fill in the details below to submit your leave application.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Select Leave Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                FilterChipRow(
                    options = leaveTypes,
                    selectedOption = selectedType,
                    onOptionSelected = { selectedType = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DatePickerField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = "Start Date",
                        modifier = Modifier.weight(1f)
                    )
                    DatePickerField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = "End Date",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FormTextField(
                    value = reason,
                    onValueChange = {
                        reason = it
                        reasonError = null
                    },
                    label = "Reason for Leave",
                    placeholder = "Describe why you need leave...",
                    singleLine = false,
                    isError = reasonError != null,
                    errorMessage = reasonError
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (isSubmitted) {
                    InfoBanner(message = "Leave application submitted! Added to Pending tab.")
                }
            }

            PrimaryButton(
                text = if (isSubmitted) "Done" else "Submit Application",
                onClick = {
                    if (isSubmitted) {
                        onBackClick()
                    } else {
                        if (reason.isBlank()) {
                            reasonError = "Please provide a reason for leave"
                        } else {
                            viewModel.applyLeave(selectedType, startDate, endDate, daysCount, reason)
                            isSubmitted = true
                        }
                    }
                }
            )
        }
    }
}
