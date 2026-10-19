package com.officemitra.app.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.model.User
import com.officemitra.app.data.preferences.UserPreferences
import com.officemitra.app.data.repository.DemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User,
    val themeMode: String = "SYSTEM"
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: DemoRepository,
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

    fun updateProfile(name: String, email: String, phone: String) {
        repository.updateProfile(name, email, phone)
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            userPreferences.setThemeMode(mode)
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            userPreferences.setLoggedIn(false)
            onLoggedOut()
        }
    }
}
