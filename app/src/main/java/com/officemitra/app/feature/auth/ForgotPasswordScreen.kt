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

                if (uiState.errorMessage != null) {
                    InfoBanner(message = uiState.errorMessage ?: "")
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (!uiState.isSubmitted) {
                    FormTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChanged,
                        label = "Email Address",
                        placeholder = "user@company.com",
                        leadingIcon = AppIcons.Email
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "Send Reset Link",
                        onClick = { viewModel.sendPasswordReset() },
                        isLoading = uiState.isLoading
                    )
                } else {
                    InfoBanner(
                        message = "Password reset link sent! Please check your email inbox."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlineButton(
                        text = "Back to Login",
                        onClick = onBackClick
                    )
                }
            }
        }
    }
}
