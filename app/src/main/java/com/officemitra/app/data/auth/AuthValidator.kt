package com.officemitra.app.data.auth

import java.util.Locale

enum class PasswordStrength { WEAK, MEDIUM, STRONG }

/**
 * Client-side validation for every auth form.
 *
 * Each `...Error` function returns a user-facing message, or `null` when the value is valid.
 * The server stays the source of truth; these checks only stop requests that can never succeed.
 */
object AuthValidator {

    const val MIN_PASSWORD_LENGTH = 8

    /** Supabase Auth hashes with bcrypt, which ignores everything after 72 bytes. */
    const val MAX_PASSWORD_LENGTH = 72
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 100
    const val MAX_EMAIL_LENGTH = 254
    const val MIN_PHONE_DIGITS = 7
    const val MAX_PHONE_DIGITS = 15

    private val EMAIL_PATTERN = Regex(
        "^[A-Za-z0-9.!#\$%&'*+/=?^_`{|}~-]+" +
            "@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?" +
            "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$"
    )

    private val PHONE_ALLOWED_PATTERN = Regex("^\\+?[0-9 ()-]+$")

    /** Trims and lower-cases an email the same way Supabase Auth stores it. */
    fun normalizeEmail(raw: String): String = raw.trim().lowercase(Locale.ROOT)

    fun isValidEmail(raw: String): Boolean = emailError(raw) == null

    fun emailError(raw: String): String? {
        val email = raw.trim()
        return when {
            email.isEmpty() -> "Email address is required"
            email.any { it.isWhitespace() } -> "Email address must not contain spaces"
            email.length > MAX_EMAIL_LENGTH -> "Email address is too long"
            !EMAIL_PATTERN.matches(email) -> "Enter a valid email address"
            else -> null
        }
    }

    /**
     * Login only checks that a password was typed. Complexity rules are deliberately not applied,
     * so accounts created under an older policy can still sign in.
     */
    fun loginPasswordError(password: String): String? =
        if (password.isEmpty()) "Password is required" else null

    /** Rules for a password that is being created or changed. */
    fun newPasswordError(password: String): String? = when {
        password.isEmpty() -> "Password is required"
        password.length < MIN_PASSWORD_LENGTH -> "Password must be at least $MIN_PASSWORD_LENGTH characters"
        password.toByteArray(Charsets.UTF_8).size > MAX_PASSWORD_LENGTH -> "Password must be $MAX_PASSWORD_LENGTH characters or fewer"
        password.none { it.isLetter() } -> "Password must include at least one letter"
        password.none { it.isDigit() } -> "Password must include at least one number"
        else -> null
    }

    fun confirmPasswordError(password: String, confirmation: String): String? = when {
        confirmation.isEmpty() -> "Please confirm your password"
        confirmation != password -> "Passwords do not match"
        else -> null
    }

    fun fullNameError(raw: String): String? {
        val name = raw.trim()
        return when {
            name.isEmpty() -> "Full name is required"
            name.length < MIN_NAME_LENGTH -> "Full name must be at least $MIN_NAME_LENGTH characters"
            name.length > MAX_NAME_LENGTH -> "Full name must be $MAX_NAME_LENGTH characters or fewer"
            name.none { it.isLetter() } -> "Enter your real name"
            else -> null
        }
    }

    fun phoneError(raw: String): String? {
        val phone = raw.trim()
        if (phone.isEmpty()) return "Mobile number is required"
        if (!PHONE_ALLOWED_PATTERN.matches(phone)) return "Enter a valid mobile number"
        val digits = phone.count { it.isDigit() }
        return if (digits < MIN_PHONE_DIGITS || digits > MAX_PHONE_DIGITS) "Enter a valid mobile number" else null
    }

    /** Keeps the leading `+` and the digits; drops spaces, brackets and dashes. */
    fun normalizePhone(raw: String): String {
        val trimmed = raw.trim()
        val digits = trimmed.filter { it.isDigit() }
        return if (trimmed.startsWith("+")) "+$digits" else digits
    }

    fun passwordStrength(password: String): PasswordStrength {
        if (newPasswordError(password) != null) return PasswordStrength.WEAK
        var score = 0
        if (password.length >= 12) score++
        if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++
        return if (score >= 2) PasswordStrength.STRONG else PasswordStrength.MEDIUM
    }
}
