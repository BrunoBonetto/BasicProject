package com.example.basicproject.home.di

import com.example.basicproject.home.data.remote.api.HomeApiService
import com.example.basicproject.home.data.remote.repository.HomeRepositoryImpl
import com.example.basicproject.home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        api: HomeApiService
    ): HomeRepository = HomeRepositoryImpl(api)

}