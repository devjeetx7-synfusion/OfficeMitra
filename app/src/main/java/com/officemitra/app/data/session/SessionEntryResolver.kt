package com.officemitra.app.data.session

import com.officemitra.app.data.preferences.UserPreferences
import com.officemitra.app.data.repository.AuthRepository
import com.officemitra.app.data.repository.AuthState
import com.officemitra.app.data.repository.OfficeMitraRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/** Works out the screen to show from the real session and the account stored on the server. */
@Singleton
class SessionEntryResolver @Inject constructor(
    private val authRepository: AuthRepository,
    private val repository: OfficeMitraRepository,
    private val userPreferences: UserPreferences
) {

    /** Called once per app start, after the stored session has been restored. */
    suspend fun onAppStart(): EntryDestination {
        if (!userPreferences.isOnboardingCompleted.first()) return EntryDestination.ONBOARDING
        return when (authRepository.awaitSession()) {
            is AuthState.SignedIn -> afterAuthentication()
            AuthState.Offline -> EntryDestination.NO_INTERNET
            else -> EntryDestination.WELCOME
        }
    }

    /** Called after a successful login, registration or email link. Loads the account first. */
    suspend fun afterAuthentication(): EntryDestination {
        if (authRepository.authState.value !is AuthState.SignedIn) return EntryDestination.WELCOME
        repository.refreshAll()
        // The session can end while the account loads (for example a revoked refresh token).
        if (authRepository.authState.value !is AuthState.SignedIn) return EntryDestination.WELCOME
        return EntryDestination.forAccount(repository.accountState.value)
    }
}
