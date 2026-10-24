package com.officemitra.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*

@Composable
fun AppSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "App Preferences",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Appearance Theme",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ThemeOptionRow(
                        title = "Light Theme",
                        selected = uiState.themeMode == "LIGHT",
                        onClick = { viewModel.setThemeMode("LIGHT") }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    ThemeOptionRow(
                        title = "Dark Theme",
                        selected = uiState.themeMode == "DARK",
                        onClick = { viewModel.setThemeMode("DARK") }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    ThemeOptionRow(
                        title = "System Default",
                        selected = uiState.themeMode == "SYSTEM",
                        onClick = { viewModel.setThemeMode("SYSTEM") }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(title: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
    }
}
