package com.officemitra.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.model.User
import com.officemitra.app.data.preferences.UserPreferences
import com.officemitra.app.data.repository.AuthRepository
import com.officemitra.app.data.repository.OfficeMitraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User,
    val themeMode: String = "SYSTEM"
)

data class EditProfileUiState(
    val nameError: String? = null,
    val phoneError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

data class LogoutUiState(
    val isLoggingOut: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: OfficeMitraRepository,
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        repository.currentUser,
        userPreferences.themeMode
    ) { user, themeMode ->
        ProfileUiState(user = user, themeMode = themeMode)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProfileUiState(user = repository.currentUser.value)
    )

    private val _editState = MutableStateFlow(EditProfileUiState())
    val editState: StateFlow<EditProfileUiState> = _editState.asStateFlow()

    private val _logoutState = MutableStateFlow(LogoutUiState())
    val logoutState: StateFlow<LogoutUiState> = _logoutState.asStateFlow()

    /** Clears errors and the "saved" confirmation as soon as the form is edited again. */
    fun onProfileEdited() {
        if (_editState.value != EditProfileUiState()) _editState.value = EditProfileUiState()
    }

    /**
     * Saves the name and phone number of the signed-in user. The email address belongs to the login
     * account and is not edited here.
     */
    fun updateProfile(name: String, phone: String) {
        if (_editState.value.isSaving) return

        val nameError = AuthValidator.fullNameError(name)
        val phoneError = AuthValidator.phoneError(phone)
        if (nameError != null || phoneError != null) {
            _editState.value = EditProfileUiState(nameError = nameError, phoneError = phoneError)
            return
        }

        _editState.value = EditProfileUiState(isSaving = true)
        viewModelScope.launch {
            val result = repository.updateProfile(name, repository.currentUser.value.email, phone)
            _editState.value = result.fold(
                // Reported only after the server stored the change.
                onSuccess = { EditProfileUiState(isSaved = true) },
                onFailure = { error ->
                    EditProfileUiState(errorMessage = AuthErrorMapper.userMessage(error, AuthAction.PROFILE))
                }
            )
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            userPreferences.setThemeMode(mode)
        }
    }

    /**
     * Ends the session. Leaving the protected screens is done by the route guard, which reacts to
     * the session itself, so this works the same whether the server could be reached or not.
     */
    fun logout() {
        if (_logoutState.value.isLoggingOut) return
        _logoutState.value = LogoutUiState(isLoggingOut = true)
        viewModelScope.launch {
            val result = authRepository.signOut()
            if (result.isSuccess) repository.clearSessionData()
            _logoutState.value = LogoutUiState(
                errorMessage = result.exceptionOrNull()
                    ?.let { error -> AuthErrorMapper.userMessage(error, AuthAction.SIGN_OUT) }
            )
        }
    }

    fun dismissLogoutError() {
        _logoutState.update { it.copy(errorMessage = null) }
    }
}
