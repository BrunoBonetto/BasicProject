package com.example.basicproject.user.di

import com.example.basicproject.core.data.AppDatabase
import com.example.basicproject.user.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
}