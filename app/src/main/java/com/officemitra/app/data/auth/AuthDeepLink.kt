package com.officemitra.app.data.auth

import java.net.URI
import java.net.URLDecoder

/** What an incoming `officemitra://auth-callback/...` link asks the app to do. */
sealed interface AuthCallback {
    val isRecovery: Boolean

    /** A PKCE authorisation code that can be exchanged for a session. */
    data class Code(val code: String, override val isRecovery: Boolean) : AuthCallback

    /** Supabase redirected back with an error, for example an expired link. */
    data class Failure(
        val errorCode: String?,
        val description: String?,
        override val isRecovery: Boolean
    ) : AuthCallback

    /** The link targets this app but carries nothing the app is willing to use. */
    data class Unsupported(override val isRecovery: Boolean) : AuthCallback
}

/**
 * The deep links Supabase Auth redirects to after an email link is opened.
 *
 * Both URLs must be listed under Authentication > URL Configuration > Redirect URLs in the
 * Supabase dashboard, otherwise Supabase falls back to the project's Site URL.
 */
object AuthDeepLink {
    const val SCHEME = "officemitra"
    const val HOST = "auth-callback"
    private const val RECOVERY_PATH = "/recovery"

    const val CONFIRM_URL = "$SCHEME://$HOST/confirm"
    const val RECOVERY_URL = "$SCHEME://$HOST$RECOVERY_PATH"

    private const val MAX_URI_LENGTH = 4096
    private val CODE_PATTERN = Regex("^[A-Za-z0-9._~-]{1,512}$")

    /**
     * Returns `null` for anything that is not an Office Mitra auth link.
     *
     * Only a PKCE `code` is accepted. Tokens in the URL fragment are ignored on purpose: the
     * intent filter is exported, so accepting ready-made tokens would let another app sign the
     * user in to an account of its choosing. A PKCE code is useless without the verifier that
     * only this installation holds.
     */
    fun parse(uri: String?): AuthCallback? {
        if (uri.isNullOrBlank() || uri.length > MAX_URI_LENGTH) return null
        val parsed = try {
            URI(uri.trim())
        } catch (_: Exception) {
            return null
        }
        if (!SCHEME.equals(parsed.scheme, ignoreCase = true)) return null
        if (!HOST.equals(parsed.host, ignoreCase = true)) return null

        val isRecovery = parsed.path.orEmpty().trimEnd('/') == RECOVERY_PATH
        val params = parseParams(parsed.rawQuery) + parseParams(parsed.rawFragment)

        val errorCode = params["error_code"] ?: params["error"]
        val errorDescription = params["error_description"]
        if (errorCode != null || errorDescription != null) {
            return AuthCallback.Failure(errorCode, errorDescription, isRecovery)
        }

        val code = parseParams(parsed.rawQuery)["code"]
        return if (code != null && CODE_PATTERN.matches(code)) {
            AuthCallback.Code(code, isRecovery)
        } else {
            AuthCallback.Unsupported(isRecovery)
        }
    }

    private fun parseParams(raw: String?): Map<String, String> {
        if (raw.isNullOrEmpty()) return emptyMap()
        return raw.split('&').mapNotNull { pair ->
            val key = pair.substringBefore('=')
            val value = pair.substringAfter('=', "")
            if (key.isEmpty()) return@mapNotNull null
            try {
                URLDecoder.decode(key, "UTF-8") to URLDecoder.decode(value, "UTF-8")
            } catch (_: Exception) {
                null
            }
        }.toMap()
    }
}
