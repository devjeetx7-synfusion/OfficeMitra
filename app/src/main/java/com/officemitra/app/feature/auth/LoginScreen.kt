package com.officemitra.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.officemitra.app.core.components.*
import com.officemitra.app.core.theme.PrimaryLight
import com.officemitra.app.core.theme.TextSecondaryLight

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Log in using your registered email or mobile number.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryLight
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoBanner(
                    message = "Demo Mode: Use any valid email & password to log in."
                )

                Spacer(modifier = Modifier.height(24.dp))

                AppTextField(
                    value = uiState.emailOrPhone,
                    onValueChange = viewModel::onEmailOrPhoneChanged,
                    label = "Email or Mobile Number",
                    placeholder = "alex.morgan@officemitra.com",
                    leadingIcon = Icons.Outlined.Email,
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChanged,
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = viewModel::onRememberMeChanged,
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryLight)
                        )
                        Text(
                            text = "Remember Me",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    TextButton(onClick = onNavigateToForgotPassword) {
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PrimaryLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Log In",
                    onClick = { viewModel.login(onNavigateToHome) },
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
                    color = TextSecondaryLight
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
