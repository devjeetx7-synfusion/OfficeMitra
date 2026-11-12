package com.officemitra.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.AppIcons
import com.officemitra.app.data.session.EntryDestination

@Composable
fun LoginScreen(
    onAuthenticated: (EntryDestination) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val inputsEnabled = !uiState.isLoading
    val submit = {
        focusManager.clearFocus()
        viewModel.login(onAuthenticated)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Log In",
            showBackButton = true,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Log in using your registered email address.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                uiState.errorMessage?.let { message ->
                    AuthErrorBanner(message = message)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                uiState.infoMessage?.let { message ->
                    AuthInfoBanner(message = message)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (uiState.canResendConfirmation) {
                    OutlineButton(
                        text = if (uiState.isResendingConfirmation) "Sending..." else "Resend Confirmation Email",
                        onClick = viewModel::resendConfirmationEmail,
                        enabled = !uiState.isResendingConfirmation && inputsEnabled
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                FormTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = "Email Address",
                    placeholder = "alex.morgan@officemitra.com",
                    leadingIcon = AppIcons.Email,
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    enabled = inputsEnabled
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChanged,
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError,
                    enabled = inputsEnabled,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { submit() })
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // The whole row toggles, so the label is part of the touch target and is read
                    // together with the checkbox by screen readers.
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .defaultMinSize(minHeight = 48.dp)
                            .toggleable(
                                value = uiState.rememberMe,
                                enabled = inputsEnabled,
                                role = Role.Checkbox,
                                onValueChange = viewModel::onRememberMeChanged
                            )
                    ) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = null,
                            enabled = inputsEnabled,
                            modifier = Modifier.padding(horizontal = 14.dp),
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = "Remember Me",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    TextButton(
                        onClick = onNavigateToForgotPassword,
                        enabled = inputsEnabled,
                        modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                    ) {
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Log In",
                    onClick = submit,
                    isLoading = uiState.isLoading
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    enabled = inputsEnabled,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                ) {
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
