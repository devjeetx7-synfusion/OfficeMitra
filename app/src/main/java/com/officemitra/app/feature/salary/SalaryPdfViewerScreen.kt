package com.officemitra.app.feature.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.ErrorColor
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun SalaryPdfViewerScreen(
    salaryId: String,
    onBackClick: () -> Unit,
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val slips by viewModel.salarySlips.collectAsState()
    val slip = slips.find { it.id == salaryId } ?: slips.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = slip?.monthYear?.let { "Salary Slip ($it)" } ?: "Salary Slip PDF",
            showBackButton = true,
            onBackClick = onBackClick,
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Outlined.Download, contentDescription = "Download PDF")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Document Header
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(ErrorColor.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Outlined.PictureAsPdf, contentDescription = null, tint = ErrorColor, modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "ACME TECHNOLOGIES CORP", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = "PAYSLIP FOR ${slip?.monthYear?.uppercase() ?: "APRIL 2024"}", style = MaterialTheme.typography.labelLarge, color = PrimaryLight)
                    }

                    // Sample Breakdown
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Text(text = "Employee: Alex Morgan (OM-EMP-2024)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text(text = "Designation: Senior Android Developer", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                        Text(text = "Pay Period: ${slip?.period ?: "01 Apr - 30 Apr 2024"}", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Basic Salary:", style = MaterialTheme.typography.bodyMedium)
                            Text(text = slip?.basicSalary ?: "$4,500.00", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "House Rent Allowance:", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "$800.00", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Tax Deductions:", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "-$1,380.00", style = MaterialTheme.typography.bodyMedium, color = ErrorColor)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "NET PAYABLE:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = slip?.netSalary ?: "$3,920.00", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryLight)
                        }
                    }

                    Text(
                        text = "System generated confidential document.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(text = "Close PDF Viewer", onClick = onBackClick)
        }
    }
}
