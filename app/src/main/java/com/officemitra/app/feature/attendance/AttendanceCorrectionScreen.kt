package com.officemitra.app.feature.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun AttendanceCorrectionScreen(
    onBackClick: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    var date by remember { mutableStateOf("03 May 2024") }
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
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Request a punch time adjustment for missing or incorrect records.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryLight
                )

                Spacer(modifier = Modifier.height(20.dp))

                AppTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = "Date",
                    leadingIcon = Icons.Outlined.CalendarToday
                )

                Spacer(modifier = Modifier.height(14.dp))

                AppTextField(
                    value = requestedTime,
                    onValueChange = { requestedTime = it },
                    label = "Requested Punch Timing",
                    leadingIcon = Icons.Outlined.Edit
                )

                Spacer(modifier = Modifier.height(14.dp))

                AppTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = "Reason for Correction",
                    placeholder = "e.g., System glitch during punch out.",
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
