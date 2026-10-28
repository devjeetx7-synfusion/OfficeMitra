package com.officemitra.app.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.preferences.UserPreferences
import com.officemitra.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashDestination {
    object Onboarding : SplashDestination()
    object Welcome : SplashDestination()
    object Home : SplashDestination()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<SplashDestination?>(null)
    val destination: StateFlow<SplashDestination?> = _destination.asStateFlow()

    init {
        checkNavigationState()
    }

    private fun checkNavigationState() {
        viewModelScope.launch {
            delay(1500) // Splash animation delay
            val isOnboardingCompleted = userPreferences.isOnboardingCompleted.first()
            val isSessionRestored = authRepository.restoreSession()

            _destination.value = when {
                !isOnboardingCompleted -> SplashDestination.Onboarding
                !isSessionRestored -> SplashDestination.Welcome
                else -> SplashDestination.Home
            }
        }
    }
}
