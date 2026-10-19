package com.officemitra.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.ErrorColor
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Change Password",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Security Settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = "Enter your current password and set a new password.", style = MaterialTheme.typography.bodySmall, color = TextSecondaryLight)

                Spacer(modifier = Modifier.height(20.dp))

                PasswordTextField(value = oldPassword, onValueChange = { oldPassword = it }, label = "Current Password")
                Spacer(modifier = Modifier.height(14.dp))
                PasswordTextField(value = newPassword, onValueChange = { newPassword = it }, label = "New Password")
                Spacer(modifier = Modifier.height(14.dp))
                PasswordTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm New Password")

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMsg!!, color = ErrorColor, style = MaterialTheme.typography.bodySmall)
                }

                if (isSuccess) {
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoBanner(message = "Password changed successfully! (Demo Mode)")
                }
            }

            PrimaryButton(
                text = if (isSuccess) "Done" else "Update Password",
                onClick = {
                    if (isSuccess) {
                        onBackClick()
                    } else {
                        if (oldPassword.isBlank() || newPassword.isBlank()) {
                            errorMsg = "All fields are required"
                        } else if (newPassword != confirmPassword) {
                            errorMsg = "New passwords do not match"
                        } else {
                            errorMsg = null
                            isSuccess = true
                        }
                    }
                }
            )
        }
    }
}
