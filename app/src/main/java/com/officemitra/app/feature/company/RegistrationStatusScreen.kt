package com.officemitra.app.feature.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.*

@Composable
fun RegistrationStatusScreen(
    onNavigateToHome: () -> Unit,
    onBackClick: () -> Unit
) {
    var selectedStatusTab by remember { mutableIntStateOf(0) } // 0: Pending, 1: Approved, 2: Rejected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Registration Status",
            showBackButton = true,
            onBackClick = onBackClick
        )

        // Demo State Switcher Tabs
        TabRow(
            selectedTabIndex = selectedStatusTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedStatusTab == 0,
                onClick = { selectedStatusTab = 0 },
                text = { Text("Pending", fontWeight = if (selectedStatusTab == 0) FontWeight.Bold else FontWeight.Medium) }
            )
            Tab(
                selected = selectedStatusTab == 1,
                onClick = { selectedStatusTab = 1 },
                text = { Text("Approved", fontWeight = if (selectedStatusTab == 1) FontWeight.Bold else FontWeight.Medium) }
            )
            Tab(
                selected = selectedStatusTab == 2,
                onClick = { selectedStatusTab = 2 },
                text = { Text("Rejected", fontWeight = if (selectedStatusTab == 2) FontWeight.Bold else FontWeight.Medium) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when (selectedStatusTab) {
                0 -> {
                    // Pending State
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(WarningLight.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Pending",
                                tint = WarningLight,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Application Under Review",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your request to join Acme Technologies Corp (Code: OM-10245) is currently awaiting HR approval.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Request Details",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Applicant: Alex Morgan", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                Text(text = "Requested Date: Today", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                Text(text = "Target Department: Software Engineering", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        InfoBanner(message = "Demo Note: You can switch tabs above to test Approved or Rejected states.")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlineButton(
                            text = "Refresh Status",
                            onClick = {}
                        )
                    }
                }

                1 -> {
                    // Approved State
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(SuccessLight.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Approved",
                                tint = SuccessLight,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Application Approved!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Welcome aboard! Your request to join Acme Technologies Corp has been approved by HR.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }

                    PrimaryButton(
                        text = "Continue to Dashboard",
                        onClick = onNavigateToHome
                    )
                }

                2 -> {
                    // Rejected State
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(ErrorLight.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Cancel,
                                contentDescription = "Rejected",
                                tint = ErrorLight,
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Application Declined",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your request to join Acme Technologies Corp could not be processed. Please verify your company code with your HR administrator.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }

                    OutlineButton(
                        text = "Back to Join Company",
                        onClick = onBackClick
                    )
                }
            }
        }
    }
}
