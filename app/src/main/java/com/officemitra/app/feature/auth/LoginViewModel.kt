package com.officemitra.app.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthErrorType
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.repository.AuthRepository
import com.officemitra.app.data.session.EntryDestination
import com.officemitra.app.data.session.SessionEntryResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    /** True after Supabase refused the login because the email address is not confirmed yet. */
    val canResendConfirmation: Boolean = false,
    val isResendingConfirmation: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val entryResolver: SessionEntryResolver
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.update {
            it.copy(email = value, emailError = null, errorMessage = null, infoMessage = null, canResendConfirmation = false)
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, errorMessage = null) }
    }

    fun onRememberMeChanged(value: Boolean) {
        _uiState.update { it.copy(rememberMe = value) }
    }

    fun login(onAuthenticated: (EntryDestination) -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        val emailError = AuthValidator.emailError(state.email)
        val passwordError = AuthValidator.loginPasswordError(state.password)
        if (emailError != null || passwordError != null) {
            _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        // Set before the coroutine starts, so a second tap in the same frame is ignored.
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null, infoMessage = null, canResendConfirmation = false)
        }
        viewModelScope.launch {
            val result = authRepository.signIn(
                email = state.email,
                password = state.password,
                keepSignedIn = state.rememberMe
            )
            result.fold(
                onSuccess = {
                    // The session exists; load the account to find out which screen it may enter.
                    val destination = entryResolver.afterAuthentication()
                    _uiState.update { it.copy(isLoading = false, password = "") }
                    onAuthenticated(destination)
                },
                onFailure = { error ->
                    val mapped = AuthErrorMapper.map(error, AuthAction.SIGN_IN)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = mapped.message,
                            emailError = if (mapped.type == AuthErrorType.INVALID_EMAIL) mapped.message else null,
                            canResendConfirmation = mapped.type == AuthErrorType.EMAIL_NOT_CONFIRMED
                        )
                    }
                }
            )
        }
    }

    fun resendConfirmationEmail() {
        val state = _uiState.value
        if (state.isResendingConfirmation || state.isLoading) return
        val emailError = AuthValidator.emailError(state.email)
        if (emailError != null) {
            _uiState.update { it.copy(emailError = emailError) }
            return
        }
        _uiState.update { it.copy(isResendingConfirmation = true, errorMessage = null, infoMessage = null) }
        viewModelScope.launch {
            val result = authRepository.resendConfirmationEmail(state.email)
            _uiState.update {
                it.copy(
                    isResendingConfirmation = false,
                    infoMessage = if (result.isSuccess) {
                        "Confirmation email sent to ${AuthValidator.normalizeEmail(state.email)}. Open the link, then log in."
                    } else null,
                    errorMessage = result.exceptionOrNull()
                        ?.let { error -> AuthErrorMapper.userMessage(error, AuthAction.RESEND_CONFIRMATION) }
                )
            }
        }
    }
}
