package com.example.basicproject.login.di

import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.data.remote.repository.LoginRepositoryImpl
import com.example.basicproject.login.data.remote.api.LoginApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        api: LoginApiService
    ): LoginRepository = LoginRepositoryImpl(api)

}