package com.officemitra.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.*
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.auth.PasswordStrength
import com.officemitra.app.data.session.EntryDestination

@Composable
fun RegisterScreen(
    onAuthenticated: (EntryDestination) -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val inputsEnabled = !uiState.isLoading
    val submit = {
        focusManager.clearFocus()
        viewModel.register(onAuthenticated)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        OfficeMitraTopBar(
            title = "Create Account",
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
                    text = "Join Office Mitra",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Create your account to start managing workforce tasks.",
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

                if (uiState.isVerificationRequired) {
                    PrimaryButton(
                        text = "Go to Login",
                        onClick = onNavigateToLogin
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlineButton(
                        text = if (uiState.isResendingConfirmation) "Sending..." else "Resend Confirmation Email",
                        onClick = viewModel::resendConfirmationEmail,
                        enabled = !uiState.isResendingConfirmation
                    )
                } else {
                    FormTextField(
                        value = uiState.fullName,
                        onValueChange = viewModel::onFullNameChanged,
                        label = "Full Name",
                        placeholder = "Alex Morgan",
                        leadingIcon = AppIcons.Person,
                        isError = uiState.nameError != null,
                        errorMessage = uiState.nameError,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        enabled = inputsEnabled
                    )

                    Spacer(modifier = Modifier.height(14.dp))

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

                    Spacer(modifier = Modifier.height(14.dp))

                    FormTextField(
                        value = uiState.mobileNumber,
                        onValueChange = viewModel::onMobileNumberChanged,
                        label = "Mobile Number",
                        placeholder = "+91 98765 43210",
                        leadingIcon = AppIcons.Phone,
                        isError = uiState.mobileError != null,
                        errorMessage = uiState.mobileError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        enabled = inputsEnabled
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PasswordTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChanged,
                        label = "Password",
                        isError = uiState.passwordError != null,
                        errorMessage = uiState.passwordError,
                        enabled = inputsEnabled,
                        imeAction = ImeAction.Next
                    )

                    // Password Strength Indicator
                    if (uiState.password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val strength = when (AuthValidator.passwordStrength(uiState.password)) {
                            PasswordStrength.WEAK -> "Weak" to ErrorLight
                            PasswordStrength.MEDIUM -> "Medium" to WarningLight
                            PasswordStrength.STRONG -> "Strong" to SuccessLight
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Text(
                                text = "Password Strength: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = strength.first,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = strength.second
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    PasswordTextField(
                        value = uiState.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChanged,
                        label = "Confirm Password",
                        isError = uiState.confirmPasswordError != null,
                        errorMessage = uiState.confirmPasswordError,
                        enabled = inputsEnabled,
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // The whole row toggles, so the label is part of the touch target and is read
                    // together with the checkbox by screen readers.
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 48.dp)
                            .toggleable(
                                value = uiState.acceptTerms,
                                enabled = inputsEnabled,
                                role = Role.Checkbox,
                                onValueChange = viewModel::onAcceptTermsChanged
                            )
                    ) {
                        Checkbox(
                            checked = uiState.acceptTerms,
                            onCheckedChange = null,
                            enabled = inputsEnabled,
                            modifier = Modifier.padding(horizontal = 14.dp),
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = "I accept the Terms and Conditions",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    uiState.termsError?.let { termsError ->
                        Text(
                            text = termsError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "Register",
                        onClick = submit,
                        isLoading = uiState.isLoading
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onNavigateToLogin,
                    enabled = inputsEnabled,
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp)
                ) {
                    Text(
                        text = "Log In",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
