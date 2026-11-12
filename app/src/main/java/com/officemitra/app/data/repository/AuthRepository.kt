package com.officemitra.app.data.repository

import com.officemitra.app.data.auth.AuthAction
import com.officemitra.app.data.auth.AuthCallback
import com.officemitra.app.data.auth.AuthDeepLink
import com.officemitra.app.data.auth.AuthErrorMapper
import com.officemitra.app.data.auth.AuthErrorType
import com.officemitra.app.data.auth.AuthException
import com.officemitra.app.data.auth.AuthValidator
import com.officemitra.app.data.preferences.UserPreferences
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

/** The app's view of the Supabase session. The Supabase session is the only source of truth. */
sealed interface AuthState {
    /** The stored session is still being read (and refreshed if it had expired). */
    data object Loading : AuthState

    data object SignedOut : AuthState

    data class SignedIn(val userId: String, val email: String?) : AuthState

    /** A stored session exists but it has expired and the server cannot be reached to refresh it. */
    data object Offline : AuthState
}

sealed interface SignUpResult {
    /** The project does not require email confirmation; the user is signed in. */
    data object Authenticated : SignUpResult

    /** Supabase accepted the request and a confirmation email is required before logging in. */
    data class ConfirmationRequired(val email: String) : SignUpResult
}

enum class AuthLinkResult { SIGNED_IN, PASSWORD_RECOVERY }

interface AuthRepository {
    val authState: StateFlow<AuthState>

    /** True after the session ended without the user asking for it (expired or revoked). */
    val sessionExpired: StateFlow<Boolean>

    /** True while an email link is being exchanged for a session. */
    val isHandlingAuthLink: StateFlow<Boolean>

    /** Suspends until the stored session has been loaded and returns the resulting state. */
    suspend fun awaitSession(): AuthState

    suspend fun signUp(email: String, password: String, fullName: String, mobile: String): Result<SignUpResult>
    suspend fun signIn(email: String, password: String, keepSignedIn: Boolean = true): Result<Unit>
    suspend fun resendConfirmationEmail(email: String): Result<Unit>
    suspend fun sendPasswordReset(email: String): Result<Unit>

    /** Changes the password of the signed-in user after verifying the current one. */
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>

    /** Sets a new password for a session that was opened from a password-reset email. */
    suspend fun completePasswordRecovery(newPassword: String): Result<Unit>

    /** Handles an `officemitra://auth-callback` link. Returns `null` if the link is not ours. */
    suspend fun handleAuthLink(uri: String): Result<AuthLinkResult>?

    /** Tries to refresh the session now, for example after connectivity returned. */
    suspend fun retrySessionRefresh(): AuthState

    /** Always ends the local session, even when the server cannot be reached. */
    suspend fun signOut(): Result<Unit>

    fun acknowledgeSessionExpired()
    fun currentUserId(): String?
    fun currentUserEmail(): String?
}

@Singleton
class SupabaseAuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val userPreferences: UserPreferences
) : AuthRepository {

    private val auth: Auth get() = supabaseClient.pluginManager.getPlugin(Auth)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _sessionExpired = MutableStateFlow(false)
    override val sessionExpired: StateFlow<Boolean> = _sessionExpired.asStateFlow()

    private val _isHandlingAuthLink = MutableStateFlow(false)
    override val isHandlingAuthLink: StateFlow<Boolean> = _isHandlingAuthLink.asStateFlow()

    /** Set while this class ends the session itself, so that it is not reported as "expired". */
    @Volatile
    private var endingSessionOnPurpose = false

    /**
     * Set when a session is ended while the library is still retrying to refresh it. The retry
     * cannot be cancelled, so if it later succeeds the restored session is signed out again.
     */
    @Volatile
    private var discardRestoredSession = false

    /** Set only by a successful password-recovery link; allows one password change without the old password. */
    @Volatile
    private var recoverySessionUserId: String? = null

    private val linkMutex = Mutex()
    private var lastHandledLink: String? = null

    init {
        scope.launch {
            // The stored session is loaded asynchronously. Reading it before this completes
            // would always report "signed out".
            runCatching { auth.awaitInitialization() }

            val keepSignedIn = runCatching { userPreferences.keepSignedIn.first() }.getOrDefault(true)
            if (!keepSignedIn && auth.sessionStatus.value !is SessionStatus.NotAuthenticated) {
                // "Remember Me" was switched off for the last login: do not carry it into a new app start.
                endLocalSession()
            }

            auth.sessionStatus.collect { status -> onSessionStatus(status) }
        }
    }

    private suspend fun onSessionStatus(status: SessionStatus) {
        if (status is SessionStatus.Authenticated && discardRestoredSession) {
            // The user signed out while offline; do not let a late refresh sign them back in.
            endingSessionOnPurpose = true
            try {
                runCatching { auth.signOut() }
                runCatching { auth.clearSession() }
            } finally {
                discardRestoredSession = false
                endingSessionOnPurpose = false
            }
            return
        }
        val previous = _authState.value
        val next: AuthState = when (status) {
            is SessionStatus.Authenticated -> {
                val user = status.session.user
                if (user != null) {
                    AuthState.SignedIn(user.id, user.email)
                } else {
                    // A session without user details: fetch them; the next status update carries the user.
                    val fetched = runCatching { auth.retrieveUserForCurrentSession(updateSession = true) }.getOrNull()
                    if (fetched != null) AuthState.SignedIn(fetched.id, fetched.email) else AuthState.Offline
                }
            }
            is SessionStatus.NotAuthenticated -> AuthState.SignedOut
            SessionStatus.NetworkError -> AuthState.Offline
            SessionStatus.LoadingFromStorage -> AuthState.Loading
        }

        if (next is AuthState.SignedIn) {
            _sessionExpired.value = false
            if (recoverySessionUserId != null && recoverySessionUserId != next.userId) recoverySessionUserId = null
        }
        if (next is AuthState.SignedOut) {
            recoverySessionUserId = null
            val hadSession = previous is AuthState.SignedIn || previous is AuthState.Offline
            if (hadSession && !endingSessionOnPurpose) _sessionExpired.value = true
        }
        _authState.value = next
    }

    override suspend fun awaitSession(): AuthState = authState.first { it !is AuthState.Loading }

    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        mobile: String
    ): Result<SignUpResult> = authCall(AuthAction.SIGN_UP) {
        requireValid(AuthValidator.fullNameError(fullName))
        requireValid(AuthValidator.emailError(email))
        requireValid(AuthValidator.phoneError(mobile))
        requireValid(AuthValidator.newPasswordError(password))
        val cleanEmail = AuthValidator.normalizeEmail(email)
        awaitSession()

        discardRestoredSession = false
        val pending = try {
            auth.signUpWith(Email, redirectUrl = AuthDeepLink.CONFIRM_URL) {
                this.email = cleanEmail
                this.password = password
                data = buildJsonObject {
                    put("full_name", fullName.trim())
                    put("phone_number", AuthValidator.normalizePhone(mobile))
                }
            }
        } catch (e: SupabaseEncodingException) {
            // The server answered 200 but not in a shape that proves an account was created.
            throw AuthException(
                AuthErrorType.SERVER,
                "We couldn't confirm that your account was created. Try logging in, or register again in a moment."
            )
        }

        if (pending == null) {
            // Confirmation is disabled for the project: Supabase returned a session.
            val userId = auth.currentUserOrNull()?.id
                ?: throw AuthException(AuthErrorType.SERVER, "Your account was created but we couldn't sign you in. Please log in.")
            userPreferences.setKeepSignedIn(true)
            awaitSignedIn(userId)
            SignUpResult.Authenticated
        } else {
            SignUpResult.ConfirmationRequired(cleanEmail)
        }
    }

    override suspend fun signIn(email: String, password: String, keepSignedIn: Boolean): Result<Unit> =
        authCall(AuthAction.SIGN_IN) {
            requireValid(AuthValidator.emailError(email))
            requireValid(AuthValidator.loginPasswordError(password))
            val cleanEmail = AuthValidator.normalizeEmail(email)
            awaitSession()

            discardRestoredSession = false
            auth.signInWith(Email) {
                this.email = cleanEmail
                this.password = password
            }
            val userId = auth.currentUserOrNull()?.id
                ?: throw AuthException(AuthErrorType.SERVER, "We couldn't start your session. Please try again.")
            userPreferences.setKeepSignedIn(keepSignedIn)
            awaitSignedIn(userId)
        }

    override suspend fun resendConfirmationEmail(email: String): Result<Unit> =
        authCall(AuthAction.RESEND_CONFIRMATION) {
            requireValid(AuthValidator.emailError(email))
            auth.resendEmail(OtpType.Email.SIGNUP, AuthValidator.normalizeEmail(email))
        }

    override suspend fun sendPasswordReset(email: String): Result<Unit> =
        authCall(AuthAction.PASSWORD_RESET_REQUEST) {
            requireValid(AuthValidator.emailError(email))
            auth.resetPasswordForEmail(
                email = AuthValidator.normalizeEmail(email),
                redirectUrl = AuthDeepLink.RECOVERY_URL
            )
        }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> =
        authCall(AuthAction.UPDATE_PASSWORD) {
            requireValid(AuthValidator.loginPasswordError(currentPassword)?.let { "Current password is required" })
            requireValid(AuthValidator.newPasswordError(newPassword))
            if (currentPassword == newPassword) {
                throw AuthException(
                    AuthErrorType.SAME_PASSWORD,
                    AuthErrorMapper.messageFor(AuthErrorType.SAME_PASSWORD, AuthAction.UPDATE_PASSWORD)
                )
            }
            val signedIn = authState.value as? AuthState.SignedIn ?: throw sessionExpiredError()
            val email = signedIn.email ?: auth.currentUserOrNull()?.email ?: throw sessionExpiredError()

            // Re-authenticate: proves that whoever holds the phone also knows the current password.
            try {
                auth.signInWith(Email) {
                    this.email = email
                    this.password = currentPassword
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (AuthErrorMapper.classify(e) == AuthErrorType.INVALID_CREDENTIALS) {
                    throw AuthException(AuthErrorType.INVALID_CREDENTIALS, "Your current password is incorrect.")
                }
                throw e
            }
            if (auth.currentUserOrNull()?.id != signedIn.userId) throw sessionExpiredError()

            auth.modifyUser { password = newPassword }
        }

    override suspend fun completePasswordRecovery(newPassword: String): Result<Unit> =
        authCall(AuthAction.UPDATE_PASSWORD) {
            requireValid(AuthValidator.newPasswordError(newPassword))
            val userId = auth.currentUserOrNull()?.id ?: throw sessionExpiredError()
            if (recoverySessionUserId != userId) {
                throw AuthException(
                    AuthErrorType.LINK_INVALID,
                    "This password reset has expired. Please request a new reset email."
                )
            }
            auth.modifyUser { password = newPassword }
            recoverySessionUserId = null
        }

    override suspend fun handleAuthLink(uri: String): Result<AuthLinkResult>? {
        val callback = AuthDeepLink.parse(uri) ?: return null
        return linkMutex.withLock {
            // The same intent can be delivered again after rotation; a code can only be used once.
            if (lastHandledLink == uri) return@withLock null
            lastHandledLink = uri
            _isHandlingAuthLink.value = true
            try {
                val result = authCall(AuthAction.AUTH_LINK) { exchange(callback) }
                val failure = result.exceptionOrNull() as? AuthException
                if (failure != null && failure.type == AuthErrorType.LINK_INVALID && !callback.isRecovery) {
                    // Supabase confirms the address before redirecting, so the account is usually usable.
                    Result.failure(
                        AuthException(
                            AuthErrorType.LINK_INVALID,
                            "We couldn't sign you in from that link. If you were confirming your email, " +
                                "it may already be confirmed - please try logging in."
                        )
                    )
                } else {
                    result
                }
            } finally {
                _isHandlingAuthLink.value = false
            }
        }
    }

    private suspend fun exchange(callback: AuthCallback): AuthLinkResult = when (callback) {
        is AuthCallback.Failure -> {
            val type = AuthErrorMapper.classifyServerMessage(callback.errorCode, callback.description)
            val safeType = if (type == AuthErrorType.UNKNOWN) AuthErrorType.LINK_INVALID else type
            throw AuthException(safeType, AuthErrorMapper.messageFor(safeType, AuthAction.AUTH_LINK))
        }
        is AuthCallback.Unsupported -> throw AuthException(
            AuthErrorType.LINK_INVALID,
            AuthErrorMapper.messageFor(AuthErrorType.LINK_INVALID, AuthAction.AUTH_LINK)
        )
        is AuthCallback.Code -> {
            awaitSession()
            discardRestoredSession = false
            val session = auth.exchangeCodeForSession(callback.code)
            val userId = session.user?.id ?: auth.currentUserOrNull()?.id
                ?: throw AuthException(AuthErrorType.SERVER, "We couldn't start your session. Please log in.")
            userPreferences.setKeepSignedIn(true)
            if (callback.isRecovery) recoverySessionUserId = userId
            awaitSignedIn(userId)
            if (callback.isRecovery) AuthLinkResult.PASSWORD_RECOVERY else AuthLinkResult.SIGNED_IN
        }
    }

    override suspend fun retrySessionRefresh(): AuthState {
        // The auth library already retries on its own schedule. Starting a second refresh with the
        // same refresh token could trip Supabase's token-reuse detection and revoke the session,
        // so this only waits for the running retry to settle.
        return withTimeoutOrNull(REFRESH_RETRY_WAIT_MS) {
            authState.first { it is AuthState.SignedIn || it is AuthState.SignedOut }
        } ?: authState.value
    }

    override suspend fun signOut(): Result<Unit> {
        endingSessionOnPurpose = true
        if (auth.sessionStatus.value is SessionStatus.NetworkError) discardRestoredSession = true
        return try {
            // Revoke the refresh token on the server when possible...
            val remote = try {
                auth.signOut()
                null
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e
            }
            // ...but the device must end up signed out even when the server is unreachable or the
            // session was already revoked.
            runCatching { auth.clearSession() }
            runCatching { userPreferences.setKeepSignedIn(true) }
            recoverySessionUserId = null
            _sessionExpired.value = false
            withTimeoutOrNull(STATE_SETTLE_TIMEOUT_MS) { authState.first { it is AuthState.SignedOut } }

            val stillSignedIn = auth.currentSessionOrNull() != null
            when {
                stillSignedIn -> Result.failure(AuthErrorMapper.map(remote ?: IllegalStateException(), AuthAction.SIGN_OUT))
                else -> Result.success(Unit)
            }
        } finally {
            endingSessionOnPurpose = false
        }
    }

    /** Clears the session on this device only, without reporting it as an expired session. */
    private suspend fun endLocalSession() {
        endingSessionOnPurpose = true
        if (auth.sessionStatus.value is SessionStatus.NetworkError) discardRestoredSession = true
        try {
            runCatching { auth.clearSession() }
            runCatching { userPreferences.setKeepSignedIn(true) }
        } finally {
            endingSessionOnPurpose = false
        }
    }

    override fun acknowledgeSessionExpired() {
        _sessionExpired.value = false
    }

    override fun currentUserId(): String? = (authState.value as? AuthState.SignedIn)?.userId

    override fun currentUserEmail(): String? = (authState.value as? AuthState.SignedIn)?.email

    private suspend fun awaitSignedIn(userId: String) {
        withTimeoutOrNull(STATE_SETTLE_TIMEOUT_MS) {
            authState.first { it is AuthState.SignedIn && it.userId == userId }
        }
    }

    private fun requireValid(error: String?) {
        if (error != null) throw AuthException(AuthErrorType.VALIDATION, error)
    }

    private fun sessionExpiredError() = AuthException(
        AuthErrorType.SESSION_EXPIRED,
        AuthErrorMapper.messageFor(AuthErrorType.SESSION_EXPIRED, AuthAction.UPDATE_PASSWORD)
    )

    /**
     * Runs an auth request and converts every failure into an [AuthException] with a safe message.
     * Cancellation is rethrown so that a closed screen does not show an error.
     */
    private suspend fun <T> authCall(action: AuthAction, block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(AuthErrorMapper.map(e, action))
    }

    private companion object {
        const val STATE_SETTLE_TIMEOUT_MS = 3_000L

        /** Slightly longer than the auth library's 10 second retry delay. */
        const val REFRESH_RETRY_WAIT_MS = 12_000L
    }
}
