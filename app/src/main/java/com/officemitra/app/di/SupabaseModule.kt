package com.officemitra.app.di

import com.officemitra.app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import java.net.URI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        val supabaseUrl = BuildConfig.SUPABASE_URL.trim()
        val supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY.trim()
        require(supabaseUrl.isNotBlank()) { "SUPABASE_URL is missing." }
        require(supabaseKey.startsWith("sb_publishable_") || supabaseKey.isNotBlank()) { "SUPABASE_PUBLISHABLE_KEY is missing." }
        require(!supabaseUrl.any { it.isWhitespace() }) { "SUPABASE_URL contains whitespace; check local.properties or Gradle configuration." }
        val parsedUrl = URI(supabaseUrl)
        require(parsedUrl.scheme == "https" && !parsedUrl.host.isNullOrBlank()) { "SUPABASE_URL must be a valid HTTPS project URL." }
        require(parsedUrl.rawPath.isNullOrEmpty() || parsedUrl.rawPath == "/") { "Use the Supabase project base URL only; do not append /auth/v1 or another API path." }
        require(parsedUrl.rawQuery == null && parsedUrl.rawFragment == null) { "SUPABASE_URL must not contain query parameters or fragments." }
        return createSupabaseClient(supabaseUrl = supabaseUrl.trimEnd('/'), supabaseKey = supabaseKey) {
            install(Auth) { flowType = FlowType.PKCE }
            install(Postgrest)
            install(Storage)
            install(Realtime)
        }
    }
}