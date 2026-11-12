package com.officemitra.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons

import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Forgot Password",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Reset Your Password",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter your registered email address to receive password reset instructions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                uiState.errorMessage?.let { message ->
                    AuthErrorBanner(message = message)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (!uiState.isSubmitted) {
                    FormTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChanged,
                        label = "Email Address",
                        placeholder = "user@company.com",
                        leadingIcon = AppIcons.Email,
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                            imeAction = androidx.compose.ui.text.input.ImeAction.Done
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = { viewModel.sendPasswordReset() }
                        ),
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "Send Reset Link",
                        onClick = { viewModel.sendPasswordReset() },
                        isLoading = uiState.isLoading
                    )
                } else {
                    AuthInfoBanner(
                        message = "If an account exists for ${uiState.email.trim()}, a password reset link is on its way. " +
                            "Open the link on this phone to choose a new password. " +
                            "Check your spam folder if it doesn't arrive within a few minutes."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlineButton(
                        text = "Back to Login",
                        onClick = onBackClick
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlineButton(
                        text = "Use a Different Email",
                        onClick = viewModel::editEmail
                    )
                }
            }
        }
    }
}
