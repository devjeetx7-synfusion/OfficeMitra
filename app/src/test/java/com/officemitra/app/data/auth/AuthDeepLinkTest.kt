package com.officemitra.app.data.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthDeepLinkTest {

    @Test
    fun `confirmation link with a pkce code is parsed`() {
        val result = AuthDeepLink.parse("officemitra://auth-callback/confirm?code=3f1c9a52-0b7e-4d1a-9c55-0a1b2c3d4e5f")
        assertEquals(AuthCallback.Code("3f1c9a52-0b7e-4d1a-9c55-0a1b2c3d4e5f", isRecovery = false), result)
    }

    @Test
    fun `recovery link is flagged as recovery`() {
        val result = AuthDeepLink.parse("officemitra://auth-callback/recovery?code=abc123")
        assertEquals(AuthCallback.Code("abc123", isRecovery = true), result)
        assertEquals(AuthCallback.Code("abc123", isRecovery = true), AuthDeepLink.parse("officemitra://auth-callback/recovery/?code=abc123"))
    }

    @Test
    fun `link without a path is treated as a sign-in link`() {
        assertEquals(AuthCallback.Code("abc", isRecovery = false), AuthDeepLink.parse("officemitra://auth-callback?code=abc"))
    }

    @Test
    fun `the configured redirect urls point at the parser`() {
        assertEquals("officemitra://auth-callback/confirm", AuthDeepLink.CONFIRM_URL)
        assertEquals("officemitra://auth-callback/recovery", AuthDeepLink.RECOVERY_URL)
        assertEquals(AuthCallback.Unsupported(isRecovery = false), AuthDeepLink.parse(AuthDeepLink.CONFIRM_URL))
        assertEquals(AuthCallback.Unsupported(isRecovery = true), AuthDeepLink.parse(AuthDeepLink.RECOVERY_URL))
    }

    @Test
    fun `error redirect in the query string is reported as a failure`() {
        val result = AuthDeepLink.parse(
            "officemitra://auth-callback/recovery?error=access_denied&error_code=otp_expired" +
                "&error_description=Email+link+is+invalid+or+has+expired"
        )
        assertEquals(
            AuthCallback.Failure("otp_expired", "Email link is invalid or has expired", isRecovery = true),
            result
        )
    }

    @Test
    fun `error redirect in the fragment is reported as a failure`() {
        val result = AuthDeepLink.parse(
            "officemitra://auth-callback/confirm#error=access_denied&error_description=Link%20expired"
        )
        assertEquals(AuthCallback.Failure("access_denied", "Link expired", isRecovery = false), result)
    }

    @Test
    fun `an error wins over a code in the same link`() {
        val result = AuthDeepLink.parse("officemitra://auth-callback/confirm?code=abc&error=server_error")
        assertTrue(result is AuthCallback.Failure)
    }

    @Test
    fun `tokens supplied directly in the link are never accepted`() {
        // An exported deep link must not let another app inject a ready-made session.
        val result = AuthDeepLink.parse(
            "officemitra://auth-callback/confirm#access_token=attacker.jwt.token&refresh_token=r&type=signup"
        )
        assertEquals(AuthCallback.Unsupported(isRecovery = false), result)
    }

    @Test
    fun `a code in the fragment is not accepted`() {
        assertEquals(
            AuthCallback.Unsupported(isRecovery = false),
            AuthDeepLink.parse("officemitra://auth-callback/confirm#code=abc")
        )
    }

    @Test
    fun `codes with unexpected characters are rejected`() {
        assertEquals(
            AuthCallback.Unsupported(isRecovery = false),
            AuthDeepLink.parse("officemitra://auth-callback/confirm?code=abc%22%7D%2C%7B")
        )
        assertEquals(
            AuthCallback.Unsupported(isRecovery = false),
            AuthDeepLink.parse("officemitra://auth-callback/confirm?code=")
        )
    }

    @Test
    fun `links for other schemes or hosts are ignored`() {
        assertNull(AuthDeepLink.parse("https://auth-callback/confirm?code=abc"))
        assertNull(AuthDeepLink.parse("officemitra://evil.example/confirm?code=abc"))
        assertNull(AuthDeepLink.parse("otherapp://auth-callback/confirm?code=abc"))
        assertNull(AuthDeepLink.parse("officemitra:auth-callback?code=abc"))
    }

    @Test
    fun `empty malformed and oversized input is ignored`() {
        assertNull(AuthDeepLink.parse(null))
        assertNull(AuthDeepLink.parse(""))
        assertNull(AuthDeepLink.parse("   "))
        assertNull(AuthDeepLink.parse("officemitra://auth-callback/confirm?code=a b"))
        assertNull(AuthDeepLink.parse("officemitra://auth-callback/confirm?code=" + "a".repeat(5000)))
    }

    @Test
    fun `scheme and host match case insensitively`() {
        assertEquals(
            AuthCallback.Code("abc", isRecovery = false),
            AuthDeepLink.parse("OfficeMitra://Auth-Callback/confirm?code=abc")
        )
    }
}
