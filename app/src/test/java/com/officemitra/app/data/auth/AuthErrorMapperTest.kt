package com.officemitra.app.data.auth

import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AuthErrorMapperTest {

    private fun classify(error: String?, description: String? = null, unauthorized: Boolean = false) =
        AuthErrorMapper.classifyServerMessage(error, description, unauthorized)

    @Test
    fun `invalid credentials are recognised in both payload formats`() {
        assertEquals(AuthErrorType.INVALID_CREDENTIALS, classify("invalid_grant", "Invalid login credentials"))
        assertEquals(AuthErrorType.INVALID_CREDENTIALS, classify("Invalid login credentials"))
    }

    @Test
    fun `unconfirmed email is recognised`() {
        assertEquals(AuthErrorType.EMAIL_NOT_CONFIRMED, classify("invalid_grant", "Email not confirmed"))
        assertEquals(AuthErrorType.EMAIL_NOT_CONFIRMED, classify("Email not confirmed"))
    }

    @Test
    fun `duplicate registration is recognised`() {
        assertEquals(AuthErrorType.EMAIL_ALREADY_REGISTERED, classify("User already registered"))
        assertEquals(
            AuthErrorType.EMAIL_ALREADY_REGISTERED,
            classify("A user with this email address has already been registered")
        )
    }

    @Test
    fun `weak and reused passwords are told apart`() {
        assertEquals(AuthErrorType.WEAK_PASSWORD, classify("Password should be at least 6 characters"))
        assertEquals(AuthErrorType.WEAK_PASSWORD, classify("Password is known to be weak and easy to guess"))
        assertEquals(
            AuthErrorType.SAME_PASSWORD,
            classify("New password should be different from the old password.")
        )
    }

    @Test
    fun `rate limits are recognised`() {
        assertEquals(AuthErrorType.RATE_LIMITED, classify("Email rate limit exceeded"))
        assertEquals(
            AuthErrorType.RATE_LIMITED,
            classify("For security purposes, you can only request this after 42 seconds.")
        )
        assertEquals(AuthErrorType.RATE_LIMITED, classify("over_email_send_rate_limit"))
    }

    @Test
    fun `disabled sign-ups and banned users are recognised`() {
        assertEquals(AuthErrorType.SIGNUP_DISABLED, classify("Signups not allowed for this instance"))
        assertEquals(AuthErrorType.SIGNUP_DISABLED, classify("Email logins are disabled"))
        assertEquals(AuthErrorType.USER_BANNED, classify("User is banned"))
    }

    @Test
    fun `expired sessions are recognised`() {
        assertEquals(AuthErrorType.SESSION_EXPIRED, classify("Invalid Refresh Token: Refresh Token Not Found"))
        assertEquals(AuthErrorType.SESSION_EXPIRED, classify("invalid_grant", "Invalid Refresh Token: Already Used"))
        assertEquals(AuthErrorType.SESSION_EXPIRED, classify("Session from session_id claim in JWT does not exist"))
        assertEquals(AuthErrorType.SESSION_EXPIRED, classify("invalid JWT: unable to parse or verify signature"))
        assertEquals(AuthErrorType.SESSION_EXPIRED, classify("Unknown error", "", unauthorized = true))
    }

    @Test
    fun `broken email links are recognised`() {
        assertEquals(AuthErrorType.LINK_INVALID, classify("invalid flow state, no valid flow state found"))
        assertEquals(AuthErrorType.LINK_INVALID, classify("code challenge does not match previously saved code verifier"))
        assertEquals(AuthErrorType.LINK_INVALID, classify("otp_expired", "Email link is invalid or has expired"))
        assertEquals(AuthErrorType.LINK_INVALID, classify("Token has expired or is invalid"))
    }

    @Test
    fun `configuration and server faults are recognised`() {
        assertEquals(AuthErrorType.CONFIGURATION, classify("Invalid API key"))
        assertEquals(AuthErrorType.SERVER, classify("Database error saving new user"))
        assertEquals(AuthErrorType.INVALID_EMAIL, classify("Unable to validate email address: invalid format"))
        assertEquals(AuthErrorType.INVALID_EMAIL, classify("Email address \"a@b.c\" is invalid"))
        assertEquals(AuthErrorType.REAUTHENTICATION_REQUIRED, classify("Password update requires reauthentication"))
    }

    @Test
    fun `unrecognised payloads fall back to unknown`() {
        assertEquals(AuthErrorType.UNKNOWN, classify("something nobody has seen before"))
        assertEquals(AuthErrorType.UNKNOWN, classify(null, null))
    }

    @Test
    fun `transport failures are classified as network or timeout`() {
        assertEquals(AuthErrorType.NETWORK, AuthErrorMapper.classify(UnknownHostException("host.supabase.co")))
        assertEquals(AuthErrorType.NETWORK, AuthErrorMapper.classify(ConnectException("refused")))
        assertEquals(AuthErrorType.NETWORK, AuthErrorMapper.classify(IOException("broken pipe")))
        assertEquals(AuthErrorType.TIMEOUT, AuthErrorMapper.classify(SocketTimeoutException("timeout")))
        assertEquals(AuthErrorType.SERVER, AuthErrorMapper.classify(SerializationException("bad json")))
        assertEquals(AuthErrorType.UNKNOWN, AuthErrorMapper.classify(IllegalStateException("boom")))
    }

    @Test
    fun `mapped messages never expose the original exception text`() {
        // Supabase REST exceptions embed the request URL and headers in their message.
        val leaky = IOException(
            "HTTP request to https://project.supabase.co/auth/v1/token failed. " +
                "Headers: [apikey=[sb_publishable_abc], Authorization=[Bearer eyJhbGciOi.secret.token]]"
        )
        val mapped = AuthErrorMapper.map(leaky, AuthAction.SIGN_IN)

        assertEquals(AuthErrorType.NETWORK, mapped.type)
        assertFalse(mapped.message.contains("Bearer"))
        assertFalse(mapped.message.contains("apikey"))
        assertFalse(mapped.message.contains("supabase.co"))
        assertFalse(mapped.message.contains("eyJ"))
        assertNull("The leaky exception must not be kept as the cause", mapped.cause)
        assertEquals("IOException", mapped.sourceName)
    }

    @Test
    fun `an auth exception is passed through unchanged`() {
        val original = AuthException(AuthErrorType.VALIDATION, "Enter a valid email address")
        assertSame(original, AuthErrorMapper.map(original, AuthAction.SIGN_UP))
    }

    @Test
    fun `every error type has a readable message for every action`() {
        AuthErrorType.values().forEach { type ->
            AuthAction.values().forEach { action ->
                val message = AuthErrorMapper.messageFor(type, action)
                assertTrue("Empty message for $type/$action", message.isNotBlank())
                assertTrue("Message for $type/$action should be a sentence", message.endsWith("."))
            }
        }
    }

    @Test
    fun `unknown errors get an action specific fallback`() {
        assertEquals(
            "We couldn't log you in. Please try again.",
            AuthErrorMapper.userMessage(IllegalStateException("x"), AuthAction.SIGN_IN)
        )
        assertEquals(
            "We couldn't create your account. Please try again.",
            AuthErrorMapper.userMessage(IllegalStateException("x"), AuthAction.SIGN_UP)
        )
    }
}
