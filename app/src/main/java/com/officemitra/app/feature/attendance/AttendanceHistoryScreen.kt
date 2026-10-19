package com.officemitra.app.feature.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.data.model.AttendanceRecord
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

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
            // Month Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "May 2024", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "Total Worked: 42h 18m", style = MaterialTheme.typography.bodySmall, color = PrimaryLight)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips
            FilterChipRow(
                options = listOf("All", "Present", "Leave", "Half Day"),
                selectedOption = selectedFilter,
                onOptionSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredRecords.isEmpty()) {
                EmptyState(
                    title = "No Records Found",
                    description = "No attendance records match the selected filter."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredRecords) { record ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedRecordForDetail = record },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(14.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = record.date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "In: ${record.punchIn} | Out: ${record.punchOut}", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    StatusBadge(status = record.status)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = record.workingDuration, style = MaterialTheme.typography.bodySmall, color = PrimaryLight)
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
            title = { Text(text = "Attendance Details - ${record.date}") },
            text = {
                Column {
                    Text("Status: ${record.status}")
                    Text("Punch In: ${record.punchIn}")
                    Text("Punch Out: ${record.punchOut}")
                    Text("Working Duration: ${record.workingDuration}")
                    Text("Location: ${record.location}")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedRecordForDetail = null
                    onNavigateToCorrection()
                }) {
                    Text("Request Correction")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedRecordForDetail = null }) {
                    Text("Close")
                }
            }
        )
    }
}
