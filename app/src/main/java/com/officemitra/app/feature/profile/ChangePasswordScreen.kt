package com.officemitra.app.feature.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*

/**
 * @param isRecovery true when opened from a password-reset email link: the current password is not
 * asked for and leaving the screen continues into the app instead of going back.
 */
@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit,
    isRecovery: Boolean = false,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val inputsEnabled = !uiState.isLoading && !uiState.isSuccess
    val submit = {
        focusManager.clearFocus()
        viewModel.updatePassword(isRecovery)
    }

    // In recovery there is no previous screen; the system back button must not leave an empty window.
    BackHandler(enabled = isRecovery, onBack = onBackClick)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = if (isRecovery) "Reset Password" else "Change Password",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Security Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isRecovery) {
                        "Choose a new password for your account."
                    } else {
                        "Enter your current password, then set a new one."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (!isRecovery) {
                    PasswordTextField(
                        value = uiState.currentPassword,
                        onValueChange = viewModel::onCurrentPasswordChanged,
                        label = "Current Password",
                        isError = uiState.currentPasswordError != null,
                        errorMessage = uiState.currentPasswordError,
                        enabled = inputsEnabled,
                        imeAction = ImeAction.Next
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
                PasswordTextField(
                    value = uiState.newPassword,
                    onValueChange = viewModel::onNewPasswordChanged,
                    label = "New Password",
                    isError = uiState.newPasswordError != null,
                    errorMessage = uiState.newPasswordError,
                    enabled = inputsEnabled,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(14.dp))
                PasswordTextField(
                    value = uiState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChanged,
                    label = "Confirm New Password",
                    isError = uiState.confirmPasswordError != null,
                    errorMessage = uiState.confirmPasswordError,
                    enabled = inputsEnabled,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { submit() })
                )

                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }

                if (uiState.isSuccess) {
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoBanner(
                        message = "Password changed successfully!",
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
            }

            PrimaryButton(
                text = if (uiState.isSuccess) "Done" else "Update Password",
                isLoading = uiState.isLoading,
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    if (uiState.isSuccess) {
                        onBackClick()
                    } else {
                        submit()
                    }
                }
            )
        }
    }
}
