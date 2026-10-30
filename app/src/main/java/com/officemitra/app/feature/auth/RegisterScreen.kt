package com.officemitra.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.officemitra.app.core.theme.*

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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

                if (uiState.errorMessage != null) {
                    InfoBanner(message = uiState.errorMessage ?: "")
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (uiState.infoMessage != null) {
                    InfoBanner(message = uiState.infoMessage ?: "")
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (uiState.isVerificationRequired) {
                    PrimaryButton(
                        text = "Go to Login",
                        onClick = onNavigateToLogin
                    )
                } else {
                    FormTextField(
                        value = uiState.fullName,
                        onValueChange = viewModel::onFullNameChanged,
                        label = "Full Name",
                        placeholder = "Alex Morgan",
                        leadingIcon = AppIcons.Person,
                        isError = uiState.nameError != null,
                        errorMessage = uiState.nameError
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FormTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChanged,
                        label = "Email Address",
                        placeholder = "alex.morgan@officemitra.com",
                        leadingIcon = AppIcons.Email,
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FormTextField(
                        value = uiState.mobileNumber,
                        onValueChange = viewModel::onMobileNumberChanged,
                        label = "Mobile Number",
                        placeholder = "+1 555-019-2834",
                        leadingIcon = AppIcons.Phone,
                        isError = uiState.mobileError != null,
                        errorMessage = uiState.mobileError
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PasswordTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChanged,
                        label = "Password",
                        isError = uiState.passwordError != null,
                        errorMessage = uiState.passwordError
                    )

                    // Password Strength Indicator
                    if (uiState.password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val strength = when {
                            uiState.password.length < 6 -> "Weak" to ErrorLight
                            uiState.password.length < 10 -> "Medium" to WarningLight
                            else -> "Strong" to SuccessLight
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
                        errorMessage = uiState.confirmPasswordError
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = uiState.acceptTerms,
                            onCheckedChange = viewModel::onAcceptTermsChanged,
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = "I accept the Terms and Conditions",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (uiState.termsError != null) {
                        Text(
                            text = uiState.termsError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "Register",
                        onClick = { viewModel.register(onNavigateToHome) },
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
