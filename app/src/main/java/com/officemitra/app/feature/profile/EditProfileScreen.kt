package com.officemitra.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf(uiState.user.name) }
    var email by remember { mutableStateOf(uiState.user.email) }
    var phone by remember { mutableStateOf(uiState.user.phone) }
    var isSaved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Edit Profile",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Update Personal Info",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Role, Company Code, and Employee ID are locked by HR policy.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                FormTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    leadingIcon = AppIcons.Person
                )

                Spacer(modifier = Modifier.height(14.dp))

                FormTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    leadingIcon = AppIcons.Email
                )

                Spacer(modifier = Modifier.height(14.dp))

                FormTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Mobile Number",
                    leadingIcon = AppIcons.Phone
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (isSaved) {
                    InfoBanner(message = "Profile updated successfully!")
                }
            }

            PrimaryButton(
                text = if (isSaved) "Done" else "Save Changes",
                onClick = {
                    if (isSaved) {
                        onBackClick()
                    } else {
                        viewModel.updateProfile(name, email, phone)
                        isSaved = true
                    }
                }
            )
        }
    }
}
