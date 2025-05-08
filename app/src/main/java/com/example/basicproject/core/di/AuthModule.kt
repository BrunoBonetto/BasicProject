package com.example.basicproject.core.di

import android.content.Context
import com.example.basicproject.core.auth.data.local.AuthLocalDataSource
import com.example.basicproject.core.auth.data.local.AuthLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthLocalDataSource(
        @ApplicationContext context: Context
    ): AuthLocalDataSource = AuthLocalDataSourceImpl(context)
}