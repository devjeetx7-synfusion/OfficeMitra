package com.officemitra.app.di

import com.officemitra.app.data.repository.AuthRepository
import com.officemitra.app.data.repository.OfficeMitraRepository
import com.officemitra.app.data.repository.SupabaseAuthRepository
import com.officemitra.app.data.repository.SupabaseOfficeMitraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: SupabaseAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindOfficeMitraRepository(
        impl: SupabaseOfficeMitraRepository
    ): OfficeMitraRepository
}
