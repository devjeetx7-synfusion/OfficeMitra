package com.officemitra.app.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthErrorType
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.repository.AuthRepository
import com.officemitra.app.data.repository.SignUpResult
import com.officemitra.app.data.session.EntryDestination
import com.officemitra.app.data.session.SessionEntryResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptTerms: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val mobileError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val isLoading: Boolean = false,
    /** True once the account exists and is waiting for the email confirmation link. */
    val isVerificationRequired: Boolean = false,
    val isResendingConfirmation: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val entryResolver: SessionEntryResolver
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /** The address the confirmation email was sent to; the form fields are cleared afterwards. */
    private var pendingEmail: String = ""

    fun onFullNameChanged(value: String) = _uiState.update { it.copy(fullName = value, nameError = null, errorMessage = null) }
    fun onEmailChanged(value: String) = _uiState.update { it.copy(email = value, emailError = null, errorMessage = null) }
    fun onMobileNumberChanged(value: String) = _uiState.update { it.copy(mobileNumber = value, mobileError = null, errorMessage = null) }
    fun onPasswordChanged(value: String) = _uiState.update { it.copy(password = value, passwordError = null, errorMessage = null) }
    fun onConfirmPasswordChanged(value: String) = _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, errorMessage = null) }
    fun onAcceptTermsChanged(value: Boolean) = _uiState.update { it.copy(acceptTerms = value, termsError = null, errorMessage = null) }

    fun register(onAuthenticated: (EntryDestination) -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        val nameError = AuthValidator.fullNameError(state.fullName)
        val emailError = AuthValidator.emailError(state.email)
        val mobileError = AuthValidator.phoneError(state.mobileNumber)
        val passwordError = AuthValidator.newPasswordError(state.password)
        val confirmError = AuthValidator.confirmPasswordError(state.password, state.confirmPassword)
        val termsError = if (state.acceptTerms) null else "You must accept the Terms and Conditions"

        if (listOf(nameError, emailError, mobileError, passwordError, confirmError, termsError).any { it != null }) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    mobileError = mobileError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError,
                    termsError = termsError
                )
            }
            return
        }

        // Set before the coroutine starts, so a second tap cannot create a second request.
        _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
        viewModelScope.launch {
            val result = authRepository.signUp(
                email = state.email,
                password = state.password,
                fullName = state.fullName,
                mobile = state.mobileNumber
            )
            result.fold(
                onSuccess = { outcome ->
                    when (outcome) {
                        SignUpResult.Authenticated -> {
                            val destination = entryResolver.afterAuthentication()
                            _uiState.update { it.copy(isLoading = false, password = "", confirmPassword = "") }
                            onAuthenticated(destination)
                        }
                        is SignUpResult.ConfirmationRequired -> {
                            pendingEmail = outcome.email
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isVerificationRequired = true,
                                    // The passwords are no longer needed; do not keep them in memory.
                                    password = "",
                                    confirmPassword = "",
                                    infoMessage = "We sent a confirmation link to ${outcome.email}. " +
                                        "Open it on this phone to activate your account, then log in. " +
                                        "If it doesn't arrive, check your spam folder. " +
                                        "If you already have an account with this email, just log in."
                                )
                            }
                        }
                    }
                },
                onFailure = { error ->
                    val mapped = AuthErrorMapper.map(error, AuthAction.SIGN_UP)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapped.message,
                            emailError = if (mapped.type == AuthErrorType.INVALID_EMAIL) mapped.message else null,
                            passwordError = if (mapped.type == AuthErrorType.WEAK_PASSWORD) mapped.message else null
                        )
                    }
                }
            )
        }
    }

    fun resendConfirmationEmail() {
        val state = _uiState.value
        if (state.isResendingConfirmation || pendingEmail.isBlank()) return
        _uiState.update { it.copy(isResendingConfirmation = true, errorMessage = null) }
        viewModelScope.launch {
            val result = authRepository.resendConfirmationEmail(pendingEmail)
            _uiState.update {
                it.copy(
                    isResendingConfirmation = false,
                    infoMessage = if (result.isSuccess) {
                        "We sent a new confirmation link to $pendingEmail. Open it on this phone, then log in."
                    } else {
                        it.infoMessage
                    },
                    errorMessage = result.exceptionOrNull()
                        ?.let { error -> AuthErrorMapper.userMessage(error, AuthAction.RESEND_CONFIRMATION) }
                )
            }
        }
    }
}
