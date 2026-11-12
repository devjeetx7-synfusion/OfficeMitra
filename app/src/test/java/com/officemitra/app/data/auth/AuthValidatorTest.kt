package com.officemitra.app.data.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidatorTest {

    // ---- Email ---------------------------------------------------------------------------

    @Test
    fun `ordinary email addresses are accepted`() {
        // Regression: the previous pattern escaped the dot twice and rejected every address.
        listOf(
            "user@example.com",
            "first.last@company.co.in",
            "user+tag@sub.domain.org",
            "u_ser-1@my-company.io",
            "USER@EXAMPLE.COM",
            "  padded@example.com  "
        ).forEach { email ->
            assertNull("Expected '$email' to be valid", AuthValidator.emailError(email))
            assertTrue(AuthValidator.isValidEmail(email))
        }
    }

    @Test
    fun `malformed email addresses are rejected`() {
        listOf(
            "plainaddress",
            "@example.com",
            "user@",
            "user@example",
            "user@.com",
            "user@example..com",
            "user@-example.com",
            "user@example.com.",
            "user@@example.com",
            "user@exa_mple.com"
        ).forEach { email ->
            assertEquals("Expected '$email' to be invalid", "Enter a valid email address", AuthValidator.emailError(email))
            assertFalse(AuthValidator.isValidEmail(email))
        }
    }

    @Test
    fun `blank email reports that it is required`() {
        assertEquals("Email address is required", AuthValidator.emailError(""))
        assertEquals("Email address is required", AuthValidator.emailError("   "))
    }

    @Test
    fun `email containing spaces is rejected with a specific message`() {
        assertEquals("Email address must not contain spaces", AuthValidator.emailError("us er@example.com"))
    }

    @Test
    fun `overlong email is rejected`() {
        val email = "a".repeat(250) + "@example.com"
        assertEquals("Email address is too long", AuthValidator.emailError(email))
    }

    @Test
    fun `email is normalised to trimmed lower case`() {
        assertEquals("user@example.com", AuthValidator.normalizeEmail("  User@Example.COM "))
    }

    // ---- Passwords -----------------------------------------------------------------------

    @Test
    fun `login only requires a non-empty password`() {
        assertEquals("Password is required", AuthValidator.loginPasswordError(""))
        // Older accounts may have short passwords; login must not block them client-side.
        assertNull(AuthValidator.loginPasswordError("abc"))
        assertNull(AuthValidator.loginPasswordError("   "))
    }

    @Test
    fun `new password must be long enough and mix letters with numbers`() {
        assertEquals("Password is required", AuthValidator.newPasswordError(""))
        assertEquals("Password must be at least 8 characters", AuthValidator.newPasswordError("abc123"))
        assertEquals("Password must include at least one number", AuthValidator.newPasswordError("abcdefgh"))
        assertEquals("Password must include at least one letter", AuthValidator.newPasswordError("12345678"))
        assertNull(AuthValidator.newPasswordError("abcd1234"))
        assertNull(AuthValidator.newPasswordError("Correct-Horse-7-Battery"))
    }

    @Test
    fun `new password longer than the bcrypt limit is rejected`() {
        val tooLong = "a1" + "b".repeat(71)
        assertEquals("Password must be 72 characters or fewer", AuthValidator.newPasswordError(tooLong))
        assertNull(AuthValidator.newPasswordError("a1" + "b".repeat(70)))
    }

    @Test
    fun `password confirmation must match exactly`() {
        assertEquals("Please confirm your password", AuthValidator.confirmPasswordError("abcd1234", ""))
        assertEquals("Passwords do not match", AuthValidator.confirmPasswordError("abcd1234", "abcd12345"))
        assertEquals("Passwords do not match", AuthValidator.confirmPasswordError("abcd1234", "Abcd1234"))
        assertNull(AuthValidator.confirmPasswordError("abcd1234", "abcd1234"))
    }

    @Test
    fun `password strength reflects length and variety`() {
        assertEquals(PasswordStrength.WEAK, AuthValidator.passwordStrength("abc"))
        assertEquals(PasswordStrength.WEAK, AuthValidator.passwordStrength("abcdefgh"))
        assertEquals(PasswordStrength.MEDIUM, AuthValidator.passwordStrength("abcd1234"))
        assertEquals(PasswordStrength.MEDIUM, AuthValidator.passwordStrength("Abcd1234"))
        assertEquals(PasswordStrength.STRONG, AuthValidator.passwordStrength("Abcd1234!"))
        assertEquals(PasswordStrength.STRONG, AuthValidator.passwordStrength("abcdefgh1234!"))
    }

    // ---- Name and phone ------------------------------------------------------------------

    @Test
    fun `full name is validated`() {
        assertEquals("Full name is required", AuthValidator.fullNameError("  "))
        assertEquals("Full name must be at least 2 characters", AuthValidator.fullNameError("A"))
        assertEquals("Enter your real name", AuthValidator.fullNameError("1234"))
        assertEquals("Full name must be 100 characters or fewer", AuthValidator.fullNameError("a".repeat(101)))
        assertNull(AuthValidator.fullNameError("Asha Kumari"))
        assertNull(AuthValidator.fullNameError("  Jean-Luc O'Neil  "))
    }

    @Test
    fun `phone number is validated`() {
        assertEquals("Mobile number is required", AuthValidator.phoneError(""))
        assertNotNull(AuthValidator.phoneError("12345"))
        assertNotNull(AuthValidator.phoneError("98765abc10"))
        assertNotNull(AuthValidator.phoneError("1234567890123456"))
        assertNotNull(AuthValidator.phoneError("+91+9876543210"))
        assertNull(AuthValidator.phoneError("9876543210"))
        assertNull(AuthValidator.phoneError("+91 98765 43210"))
        assertNull(AuthValidator.phoneError("+1 (555) 019-2834"))
    }

    @Test
    fun `phone number is normalised to digits with an optional plus`() {
        assertEquals("+919876543210", AuthValidator.normalizePhone(" +91 98765-43210 "))
        assertEquals("5550192834", AuthValidator.normalizePhone("(555) 019-2834"))
    }
}
