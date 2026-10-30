package com.officemitra.app.feature.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*

@Composable
fun SalarySlipsScreen(
    onNavigateToPdfViewer: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val salarySlips by viewModel.salarySlips.collectAsState()
    var selectedYear by remember { mutableStateOf("2025") }
    var downloadMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        ) {
        OfficeMitraTopBar(
            title = "Salary Slips",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(16.dp)) {
            FilterChipRow(
                options = listOf("2025", "2024"),
                selectedOption = selectedYear,
                onOptionSelected = { selectedYear = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (salarySlips.isEmpty()) {
                EmptyState(
                    title = "No Salary Slips",
                    description = "No published salary slips found for your account."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(salarySlips) { slip ->
                        SalarySlipCard(
                            monthYear = slip.monthYear,
                            period = slip.period,
                            publishDate = slip.publishDate,
                            onViewPdf = { onNavigateToPdfViewer(slip.id) },
                            onDownloadPdf = { downloadMessage = "Downloading PDF for ${slip.monthYear}..." }
                        )
                    }
                }
            }
        }
    }

    downloadMessage?.let { msg ->
        ConfirmationDialog(
            title = "Download PDF",
            message = msg,
            confirmText = "OK",
            onConfirm = { downloadMessage = null },
            onDismiss = { downloadMessage = null }
        )
    }
}
