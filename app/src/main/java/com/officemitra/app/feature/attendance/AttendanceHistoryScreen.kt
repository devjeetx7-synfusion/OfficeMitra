package com.officemitra.app.feature.attendance

import androidx.compose.foundation.BorderStroke
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
import com.officemitra.app.data.model.AttendanceRecord

@Composable
fun AttendanceHistoryScreen(
    onBackClick: () -> Unit,
    onNavigateToCorrection: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedRecordForDetail by remember { mutableStateOf<AttendanceRecord?>(null) }

    val filteredRecords = uiState.historyRecords.filter { record ->
        when (selectedFilter.lowercase()) {
            "all" -> true
            else -> record.status.lowercase() == selectedFilter.lowercase()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Attendance History",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Month Summary Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Month History",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Total Worked: 42h 18m",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Chips
            FilterChipRow(
                options = listOf("All", "Present", "Leave", "Half Day"),
                selectedOption = selectedFilter,
                onOptionSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (filteredRecords.isEmpty()) {
                EmptyState(
                    title = "No Records Found",
                    description = "No attendance records match the selected filter."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRecords) { record ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedRecordForDetail = record },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = record.date,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "In: ${record.punchIn} • Out: ${record.punchOut}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    StatusChip(status = record.status)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = record.workingDuration,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedRecordForDetail?.let { record ->
        AlertDialog(
            onDismissRequest = { selectedRecordForDetail = null },
            title = {
                Text(
                    text = "Attendance Detail",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Date: ${record.date}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Status: ${record.status}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Punch In: ${record.punchIn}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Punch Out: ${record.punchOut}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Working Duration: ${record.workingDuration}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Location: ${record.location}", style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedRecordForDetail = null
                    onNavigateToCorrection()
                }) {
                    Text("Request Correction", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedRecordForDetail = null }) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(18.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
