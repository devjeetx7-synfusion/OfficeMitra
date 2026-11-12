package com.officemitra.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    private val DARK_MODE_THEME = stringPreferencesKey("dark_mode_theme") // "LIGHT", "DARK", "SYSTEM"
    private val KEEP_SIGNED_IN = booleanPreferencesKey("keep_signed_in")

    // Removed: the Supabase session is the only record of whether a user is signed in.
    private val LEGACY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[ONBOARDING_COMPLETED] ?: false }

    val themeMode: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[DARK_MODE_THEME] ?: "SYSTEM" }

    /** False when the last login had "Remember Me" switched off; the session then ends with the app. */
    val keepSignedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEEP_SIGNED_IN] ?: true }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_THEME] = mode
        }
    }

    suspend fun setKeepSignedIn(keep: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEEP_SIGNED_IN] = keep
            preferences.remove(LEGACY_IS_LOGGED_IN)
        }
    }
}
