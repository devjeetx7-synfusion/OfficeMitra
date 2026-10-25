package com.officemitra.app.feature.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons

@Composable
fun AttendanceCorrectionScreen(
    onBackClick: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    var date by remember { mutableStateOf("03 May") }
    var requestedTime by remember { mutableStateOf("09:00 AM - 06:00 PM") }
    var reason by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Attendance Correction",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Submit Correction Request",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Request a punch time adjustment for missing or incorrect attendance records.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                FormTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = "Date",
                    leadingIcon = AppIcons.Calendar
                )

                Spacer(modifier = Modifier.height(14.dp))

                FormTextField(
                    value = requestedTime,
                    onValueChange = { requestedTime = it },
                    label = "Requested Punch Timing",
                    leadingIcon = AppIcons.Edit
                )

                Spacer(modifier = Modifier.height(14.dp))

                FormTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = "Reason for Correction",
                    placeholder = "e.g., System update prevented punch out.",
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (isSubmitted) {
                    InfoBanner(message = "Correction request submitted successfully! Pending manager approval.")
                }
            }

            PrimaryButton(
                text = if (isSubmitted) "Done" else "Submit Request",
                onClick = {
                    if (isSubmitted) {
                        onBackClick()
                    } else {
                        if (reason.isNotBlank()) {
                            viewModel.submitCorrection(date, requestedTime, reason)
                            isSubmitted = true
                        }
                    }
                }
            )
        }
    }
}
