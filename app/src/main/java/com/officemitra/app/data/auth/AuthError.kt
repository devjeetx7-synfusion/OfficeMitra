package com.officemitra.app.data.auth

import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.SocketTimeoutException

/** The operation that was running when an error happened; picks the fallback message. */
enum class AuthAction {
    SIGN_IN,
    SIGN_UP,
    SIGN_OUT,
    PASSWORD_RESET_REQUEST,
    RESEND_CONFIRMATION,
    UPDATE_PASSWORD,
    AUTH_LINK,
    PROFILE
}

enum class AuthErrorType {
    VALIDATION,
    INVALID_CREDENTIALS,
    EMAIL_NOT_CONFIRMED,
    EMAIL_ALREADY_REGISTERED,
    WEAK_PASSWORD,
    SAME_PASSWORD,
    INVALID_EMAIL,
    RATE_LIMITED,
    SIGNUP_DISABLED,
    USER_BANNED,
    SESSION_EXPIRED,
    REAUTHENTICATION_REQUIRED,
    LINK_INVALID,
    NETWORK,
    TIMEOUT,
    CONFIGURATION,
    SERVER,
    UNKNOWN
}

/**
 * The only exception type the auth layer hands to the UI.
 *
 * [message] is always safe to display. The original exception is intentionally not kept as the
 * cause: Supabase REST exceptions embed the request URL and headers (API key and bearer token) in
 * their message, and those must never reach the screen or the logs.
 */
class AuthException(
    val type: AuthErrorType,
    override val message: String,
    val sourceName: String? = null
) : Exception(message)

object AuthErrorMapper {

    fun map(error: Throwable, action: AuthAction): AuthException {
        if (error is AuthException) return error
        val type = classify(error)
        return AuthException(type, messageFor(type, action), error::class.simpleName)
    }

    fun classify(error: Throwable): AuthErrorType = when (error) {
        is AuthException -> error.type
        is RestException -> classifyServerMessage(
            error = error.error,
            description = error.description,
            isUnauthorized = error is UnauthorizedRestException
        )
        is HttpRequestTimeoutException, is SocketTimeoutException -> AuthErrorType.TIMEOUT
        is HttpRequestException ->
            if (error.message.orEmpty().contains("timed out", ignoreCase = true) ||
                error.message.orEmpty().contains("timeout", ignoreCase = true)
            ) AuthErrorType.TIMEOUT else AuthErrorType.NETWORK
        is IOException -> AuthErrorType.NETWORK
        is SupabaseEncodingException, is SerializationException -> AuthErrorType.SERVER
        else ->
            // Ktor's CIO engine reports DNS failures with this non-IOException type.
            if (error::class.simpleName == "UnresolvedAddressException") AuthErrorType.NETWORK
            else AuthErrorType.UNKNOWN
    }

    /**
     * Classifies a Supabase Auth / PostgREST error payload.
     *
     * Both the current (`error_code` + `msg`) and the legacy (`error` + `error_description`)
     * payloads are handled, which is why the match is done on text rather than on one field.
     */
    fun classifyServerMessage(
        error: String?,
        description: String?,
        isUnauthorized: Boolean = false
    ): AuthErrorType {
        val text = listOfNotNull(error, description).joinToString(" ").lowercase()
        fun has(vararg needles: String) = needles.any { it in text }
        return when {
            has("invalid login credentials", "invalid_credentials") -> AuthErrorType.INVALID_CREDENTIALS
            has("email not confirmed", "email_not_confirmed") -> AuthErrorType.EMAIL_NOT_CONFIRMED
            has("already registered", "already been registered", "user_already_exists", "email_exists") ->
                AuthErrorType.EMAIL_ALREADY_REGISTERED
            has("same_password", "different from the old password") -> AuthErrorType.SAME_PASSWORD
            has("weak_password", "password should", "known to be weak", "pwned") -> AuthErrorType.WEAK_PASSWORD
            has("reauthentication") -> AuthErrorType.REAUTHENTICATION_REQUIRED
            has(
                "rate limit", "rate_limit", "too many requests",
                "for security purposes, you can only request this"
            ) -> AuthErrorType.RATE_LIMITED
            has(
                "signups not allowed", "signup_disabled", "signups are disabled",
                "email_provider_disabled", "logins are disabled"
            ) -> AuthErrorType.SIGNUP_DISABLED
            has("user is banned", "user_banned") -> AuthErrorType.USER_BANNED
            has("invalid api key", "no api key found", "invalid_api_key") -> AuthErrorType.CONFIGURATION
            has(
                "flow state", "flow_state", "code verifier", "bad_code_verifier", "otp_expired",
                "invalid or has expired", "expired or is invalid", "auth code"
            ) -> AuthErrorType.LINK_INVALID
            has(
                "refresh token", "refresh_token", "session_not_found", "session not found",
                "session_id claim", "user from sub claim",
                "auth session missing", "invalid jwt", "bad_jwt", "jwt expired", "token is expired",
                "sub claim in jwt does not exist"
            ) -> AuthErrorType.SESSION_EXPIRED
            has("unable to validate email address", "email_address_invalid", "invalid format") ||
                ("email address" in text && "invalid" in text) -> AuthErrorType.INVALID_EMAIL
            has("database error", "unexpected_failure", "captcha") -> AuthErrorType.SERVER
            isUnauthorized -> AuthErrorType.SESSION_EXPIRED
            else -> AuthErrorType.UNKNOWN
        }
    }

    fun messageFor(type: AuthErrorType, action: AuthAction): String = when (type) {
        AuthErrorType.VALIDATION -> "Please check the details you entered and try again."
        AuthErrorType.INVALID_CREDENTIALS -> "Incorrect email or password. Please try again."
        AuthErrorType.EMAIL_NOT_CONFIRMED ->
            "Your email address is not confirmed yet. Open the confirmation link we emailed you, then log in."
        AuthErrorType.EMAIL_ALREADY_REGISTERED ->
            "An account with this email already exists. Log in instead, or reset your password."
        AuthErrorType.WEAK_PASSWORD ->
            "That password is too weak. Use at least ${AuthValidator.MIN_PASSWORD_LENGTH} characters " +
                "with letters and numbers, and avoid common passwords."
        AuthErrorType.SAME_PASSWORD -> "Your new password must be different from your current password."
        AuthErrorType.INVALID_EMAIL -> "That email address was not accepted. Please check it and try again."
        AuthErrorType.RATE_LIMITED -> "Too many attempts. Please wait a minute and try again."
        AuthErrorType.SIGNUP_DISABLED ->
            "Email sign-in is currently disabled for this app. Please contact your administrator."
        AuthErrorType.USER_BANNED -> "This account has been disabled. Please contact your administrator."
        AuthErrorType.SESSION_EXPIRED -> "Your session has expired. Please log in again."
        AuthErrorType.REAUTHENTICATION_REQUIRED ->
            "For your security, please log out and log in again before changing your password."
        AuthErrorType.LINK_INVALID ->
            "This link is invalid or has expired. Please request a new one from the app on this device."
        AuthErrorType.NETWORK -> "Can't reach Office Mitra. Check your internet connection and try again."
        AuthErrorType.TIMEOUT -> "The request timed out. Check your connection and try again."
        AuthErrorType.CONFIGURATION ->
            "The app can't connect to its server because of a configuration problem. Please contact support."
        AuthErrorType.SERVER -> "Something went wrong on our side. Please try again in a moment."
        AuthErrorType.UNKNOWN -> when (action) {
            AuthAction.SIGN_IN -> "We couldn't log you in. Please try again."
            AuthAction.SIGN_UP -> "We couldn't create your account. Please try again."
            AuthAction.SIGN_OUT -> "We couldn't log you out. Please try again."
            AuthAction.PASSWORD_RESET_REQUEST -> "We couldn't send the reset email. Please try again."
            AuthAction.RESEND_CONFIRMATION -> "We couldn't resend the confirmation email. Please try again."
            AuthAction.UPDATE_PASSWORD -> "We couldn't update your password. Please try again."
            AuthAction.AUTH_LINK -> "We couldn't complete that link. Please try again."
            AuthAction.PROFILE -> "We couldn't save your profile. Please try again."
        }
    }

    /** Message for any throwable shown outside the auth repository (for example profile saves). */
    fun userMessage(error: Throwable, action: AuthAction): String = map(error, action).message
}
