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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        val supabaseUrl = BuildConfig.SUPABASE_URL
        val supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY

        return createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Auth) {
                flowType = FlowType.PKCE
            }
            install(Postgrest)
            install(Storage)
            install(Realtime)
        }
    }
}
