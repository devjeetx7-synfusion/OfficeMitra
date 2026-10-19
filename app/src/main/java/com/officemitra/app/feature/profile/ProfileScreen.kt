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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.ErrorColor
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

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

            // Profile Header Card
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
                    EmployeeAvatar(
                        imageUrl = uiState.user.avatarUrl,
                        name = uiState.user.name,
                        size = 72
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = uiState.user.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${uiState.user.designation} • ${uiState.user.department}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryLight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StatusBadge(status = uiState.user.employeeId)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Personal Information
            SectionHeader(
                title = "Personal Information",
                actionText = "Edit",
                onActionClick = onNavigateToEditProfile
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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

            Spacer(modifier = Modifier.height(20.dp))

            // Settings & Preferences
            SectionHeader(title = "Account Settings")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
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
                        title = "App Preferences",
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
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorColor.copy(alpha = 0.12f), contentColor = ErrorColor)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = "Logout")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
private fun InfoRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = TextSecondaryLight, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SettingOptionRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = PrimaryLight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
        Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = null, tint = TextSecondaryLight)
    }
}
