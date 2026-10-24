package com.officemitra.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*

@Composable
fun ProfileScreen(
    onNavigateToEditProfile: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToAppSettings: () -> Unit,
    onNavigateToAboutApp: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(title = "My Profile")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Violet-Blue Gradient Profile Header Card
            ProfileHeader(
                name = uiState.user.name,
                employeeId = uiState.user.employeeId,
                companyName = uiState.user.companyName,
                department = uiState.user.department,
                designation = uiState.user.designation,
                imageUrl = uiState.user.avatarUrl,
                onEditClick = onNavigateToEditProfile
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Personal Information
            SectionHeader(
                title = "Personal Information",
                actionText = "Edit",
                onActionClick = onNavigateToEditProfile
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Email Address", value = uiState.user.email, icon = Icons.Outlined.Email)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(label = "Mobile Number", value = uiState.user.phone, icon = Icons.Outlined.Phone)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(label = "Company Name", value = uiState.user.companyName, icon = Icons.Outlined.Business)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    InfoRow(label = "Joining Date", value = uiState.user.joiningDate, icon = Icons.Outlined.CalendarToday)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Settings & Preferences
            SectionHeader(title = "Account Settings")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    SettingOptionRow(
                        title = "Edit Profile Info",
                        icon = Icons.Outlined.Person,
                        onClick = onNavigateToEditProfile
                    )
                    SettingOptionRow(
                        title = "Change Password",
                        icon = Icons.Outlined.Lock,
                        onClick = onNavigateToChangePassword
                    )
                    SettingOptionRow(
                        title = "App Preferences & Theme",
                        icon = Icons.Outlined.Settings,
                        onClick = onNavigateToAppSettings
                    )
                    SettingOptionRow(
                        title = "About Office Mitra",
                        icon = Icons.Outlined.Info,
                        onClick = onNavigateToAboutApp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = "Logout")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showLogoutDialog) {
        ConfirmationDialog(
            title = "Log Out",
            message = "Are you sure you want to log out of Office Mitra?",
            confirmText = "Log Out",
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout(onLogout)
            },
            onDismiss = { showLogoutDialog = false },
            isDanger = true
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun SettingOptionRow(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
