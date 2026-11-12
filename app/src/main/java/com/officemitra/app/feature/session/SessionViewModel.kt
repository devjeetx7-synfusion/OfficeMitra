package com.officemitra.app.feature.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthDeepLink
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.repository.AuthLinkResult
import com.officemitra.app.data.repository.AuthRepository
import com.officemitra.app.data.repository.AuthState
import com.officemitra.app.data.session.EntryDestination
import com.officemitra.app.data.session.SessionEntryResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @param isStartup true for the decision taken when the app process starts. After the system
 * restored a previous screen, that decision must not throw the user back to the dashboard.
 */
data class SessionNavigation(val destination: EntryDestination, val isStartup: Boolean = false)

/**
 * Activity-scoped owner of session-driven navigation: the start-up decision, email links,
 * retry after a connection problem and sign-out from screens that have no other way out.
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val entryResolver: SessionEntryResolver
) : ViewModel() {

    val authState: StateFlow<AuthState> = authRepository.authState

    private val _navigation = Channel<SessionNavigation>(Channel.BUFFERED)

    /** One-shot requests to replace the whole back stack with a destination. */
    val navigation: Flow<SessionNavigation> = _navigation.receiveAsFlow()

    private val _notice = MutableStateFlow<String?>(null)

    /** A message that must be acknowledged, for example a failed email link. */
    val notice: StateFlow<String?> = _notice.asStateFlow()

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

    private var linkJob: Job? = null
    private var linkDecidedDestination = false
    private var actionJob: Job? = null

    init {
        viewModelScope.launch {
            delay(SPLASH_DURATION_MS)
            // An email link that launched the app decides the destination itself.
            linkJob?.join()
            if (!linkDecidedDestination) {
                _navigation.send(SessionNavigation(entryResolver.onAppStart(), isStartup = true))
            }
        }
    }

    /** Handles the data URI of a launch intent. Anything that is not an Office Mitra auth link is ignored. */
    fun onAuthLink(uri: String?) {
        if (uri == null || AuthDeepLink.parse(uri) == null) return
        val previous = linkJob
        linkJob = viewModelScope.launch {
            previous?.join()
            val result = authRepository.handleAuthLink(uri) ?: return@launch
            result.fold(
                onSuccess = { outcome ->
                    linkDecidedDestination = true
                    when (outcome) {
                        AuthLinkResult.PASSWORD_RECOVERY -> _navigation.send(SessionNavigation(EntryDestination.RESET_PASSWORD))
                        AuthLinkResult.SIGNED_IN -> _navigation.send(SessionNavigation(entryResolver.afterAuthentication()))
                    }
                },
                onFailure = { error ->
                    _notice.value = AuthErrorMapper.userMessage(error, AuthAction.AUTH_LINK)
                }
            )
        }
    }

    /** Re-evaluates where the user belongs, after a connection or loading problem. */
    fun retryEntry() {
        if (actionJob?.isActive == true) return
        actionJob = viewModelScope.launch {
            _isBusy.value = true
            try {
                if (authRepository.authState.value is AuthState.Offline) authRepository.retrySessionRefresh()
                _navigation.send(SessionNavigation(entryResolver.onAppStart()))
            } finally {
                _isBusy.value = false
            }
        }
    }

    /** Signs out. Navigation follows from the session state, see [consumeSessionExpired]. */
    fun signOut() {
        if (actionJob?.isActive == true) return
        actionJob = viewModelScope.launch {
            _isBusy.value = true
            try {
                authRepository.signOut().onFailure { error ->
                    _notice.value = AuthErrorMapper.userMessage(error, AuthAction.SIGN_OUT)
                }
            } finally {
                _isBusy.value = false
            }
        }
    }

    /**
     * Tells the route guard whether the session ended on its own (expired or revoked) rather than
     * by the user logging out, and clears the flag.
     */
    fun consumeSessionExpired(): Boolean {
        val expired = authRepository.sessionExpired.value
        if (expired) authRepository.acknowledgeSessionExpired()
        return expired
    }

    fun dismissNotice() {
        _notice.value = null
    }

    private companion object {
        /** Lets the splash animation finish; the session itself is awaited separately. */
        const val SPLASH_DURATION_MS = 1_500L
    }
}
