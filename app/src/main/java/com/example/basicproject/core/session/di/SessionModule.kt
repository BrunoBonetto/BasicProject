package com.example.basicproject.core.session.di

import com.example.basicproject.core.auth.data.local.AuthLocalDataSource
import com.example.basicproject.core.session.data.SessionManagerImpl
import com.example.basicproject.core.session.domain.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    @Provides
    @Singleton
    fun provideSessionManager(
        authLocalDataSource: AuthLocalDataSource
    ): SessionManager {
        return SessionManagerImpl(authLocalDataSource)
    }
}