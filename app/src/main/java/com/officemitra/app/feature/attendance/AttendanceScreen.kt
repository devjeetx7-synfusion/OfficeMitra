package com.officemitra.app.feature.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.SuccessColor
import com.officemitra.app.core.theme.TextSecondaryLight
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AttendanceScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToCorrection: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentTime by remember { mutableStateOf(SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())) }
    var showCameraPreviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Attendance Tracking",
            actions = {
                IconButton(onClick = onNavigateToHistory) {
                    Icon(imageVector = Icons.Outlined.History, contentDescription = "History")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Live Clock Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentTime,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryLight
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    StatusBadge(status = if (uiState.isPunchedIn) "Punched In" else "Not Punched")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Location Indicator Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SuccessColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null, tint = SuccessColor)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Location Verification", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = uiState.locationStatus, style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Camera Selfie UI Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Verification Selfie", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showCameraPreviewDialog = true }) {
                            Icon(imageVector = Icons.Outlined.CameraAlt, contentDescription = "Camera", tint = PrimaryLight)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, PrimaryLight.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Outlined.CameraAlt, contentDescription = null, tint = TextSecondaryLight, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Tap camera to capture selfie verification photo", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Punch Button
            PrimaryButton(
                text = if (uiState.isPunchedIn) "Punch Out Now" else "Punch In Now",
                onClick = viewModel::punchInOrOut
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlineButton(
                text = "Request Attendance Correction",
                onClick = onNavigateToCorrection
            )
        }
    }

    if (showCameraPreviewDialog) {
        AlertDialog(
            onDismissRequest = { showCameraPreviewDialog = false },
            title = { Text("Selfie Preview (UI Camera Demo)") },
            text = { Text("Selfie captured and verified locally for punch action.") },
            confirmButton = {
                TextButton(onClick = { showCameraPreviewDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
