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

    override suspend fun signUp(email: String, pass: String, fullName: String, mobile: String): Result<SignUpResult> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = pass
                data = buildJsonObject {
                    put("full_name", fullName)
                    put("phone_number", mobile)
                }
            }
            val user = auth.currentUserOrNull()
            if (user != null) {
                userPreferences.setLoggedIn(true)
                Result.success(SignUpResult.Authenticated)
            } else {
                userPreferences.setLoggedIn(false)
                Result.success(SignUpResult.VerificationRequired)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, pass: String): Result<Unit> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            val user = auth.currentUserOrNull()
            if (user != null) {
                userPreferences.setLoggedIn(true)
                Result.success(Unit)
            } else {
                userPreferences.setLoggedIn(false)
                Result.failure(IllegalStateException("Login failed: User session not established."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            userPreferences.setLoggedIn(false)
            Result.success(Unit)
        } catch (e: Exception) {
            userPreferences.setLoggedIn(false)
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            auth.modifyUser {
                password = newPassword
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun restoreSession(): Boolean {
        return try {
            val user: UserInfo? = auth.currentUserOrNull()
            val hasValidUser = user != null
            userPreferences.setLoggedIn(hasValidUser)
            hasValidUser
        } catch (e: Exception) {
            userPreferences.setLoggedIn(false)
            false
        }
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUserOrNull()?.id
    }
}
