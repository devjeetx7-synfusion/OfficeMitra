package com.officemitra.app.di

import com.officemitra.app.BuildConfig
import com.officemitra.app.data.auth.AuthDeepLink
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    /**
     * The single Supabase client of the app.
     *
     * Only the project URL and the publishable (anon) key are used. They come from the build
     * configuration and are public by design; what a signed-in user may read or write is decided by
     * row level security on the server. A service-role or secret key must never be put in this app,
     * and the Gradle build refuses one.
     */
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
        ) {
            // The library logs failed requests together with their headers, which include the
            // user's access token. Nothing is logged in release builds.
            defaultLogLevel = if (BuildConfig.DEBUG) LogLevel.INFO else LogLevel.NONE
            requestTimeout = 20.seconds

            install(Auth) {
                // PKCE: email links return a one-time code that only this installation can redeem.
                flowType = FlowType.PKCE
                scheme = AuthDeepLink.SCHEME
                host = AuthDeepLink.HOST
                // The session is persisted on the device, restored on start and refreshed before
                // it expires. These are the library defaults, stated here because the app relies on them.
                autoLoadFromStorage = true
                autoSaveToStorage = true
                alwaysAutoRefresh = true
            }
            install(Postgrest)
            install(Storage)
            install(Realtime)
        }
    }
}
