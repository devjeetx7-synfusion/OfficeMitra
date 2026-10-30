package com.officemitra.app.data.repository

import com.officemitra.app.data.preferences.UserPreferences
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

sealed class SignUpResult {
    object Authenticated : SignUpResult()
    object VerificationRequired : SignUpResult()
}

interface AuthRepository {
    suspend fun signUp(email: String, pass: String, fullName: String, mobile: String): Result<SignUpResult>
    suspend fun signIn(email: String, pass: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    suspend fun restoreSession(): Boolean
    fun getCurrentUserId(): String?
}

@Singleton
class SupabaseAuthRepository @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val userPreferences: UserPreferences
) : AuthRepository {
    private val auth: Auth get() = supabaseClient.pluginManager.getPlugin(Auth)

    private fun normalizedEmail(value: String): String {
        val email = value.trim()
        require(email.isNotEmpty()) { "Email address is required." }
        require(!email.any { it.isWhitespace() }) { "Email address must not contain spaces." }
        require(EMAIL_PATTERN.matches(email)) { "Enter a valid email address." }
        return email
    }

    override suspend fun signUp(email: String, pass: String, fullName: String, mobile: String): Result<SignUpResult> = try {
        val cleanEmail = normalizedEmail(email)
        require(pass.length >= 6) { "Password must contain at least 6 characters." }
        auth.signUpWith(Email) {
            this.email = cleanEmail
            this.password = pass
            data = buildJsonObject {
                put("full_name", fullName.trim())
                put("phone_number", mobile.trim())
            }
        }
        if (auth.currentUserOrNull() != null) {
            userPreferences.setLoggedIn(true)
            Result.success(SignUpResult.Authenticated)
        } else {
            userPreferences.setLoggedIn(false)
            Result.success(SignUpResult.VerificationRequired)
        }
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun signIn(email: String, pass: String): Result<Unit> = try {
        auth.signInWith(Email) {
            this.email = normalizedEmail(email)
            this.password = pass
        }
        if (auth.currentUserOrNull() != null) {
            userPreferences.setLoggedIn(true)
            Result.success(Unit)
        } else {
            userPreferences.setLoggedIn(false)
            Result.failure(IllegalStateException("Login failed: User session not established."))
        }
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun signOut(): Result<Unit> = try {
        auth.signOut()
        userPreferences.setLoggedIn(false)
        Result.success(Unit)
    } catch (e: Exception) {
        userPreferences.setLoggedIn(false)
        Result.failure(e)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = try {
        auth.resetPasswordForEmail(normalizedEmail(email))
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun updatePassword(newPassword: String): Result<Unit> = try {
        require(newPassword.length >= 6) { "Password must contain at least 6 characters." }
        auth.modifyUser { password = newPassword }
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun restoreSession(): Boolean = try {
        val user: UserInfo? = auth.currentUserOrNull()
        val authenticated = user != null
        userPreferences.setLoggedIn(authenticated)
        authenticated
    } catch (e: Exception) {
        userPreferences.setLoggedIn(false)
        false
    }

    override fun getCurrentUserId(): String? = auth.currentUserOrNull()?.id

    private companion object {
        val EMAIL_PATTERN = Regex("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$")
    }
}