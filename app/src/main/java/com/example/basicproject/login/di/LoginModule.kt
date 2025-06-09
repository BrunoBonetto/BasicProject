package com.example.basicproject.login.di

import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.usecase.GetProductsUseCase
import com.example.basicproject.login.data.remote.api.LoginApiService
import com.example.basicproject.login.data.remote.repository.LoginRepositoryImpl
import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.domain.usecase.LoginUseCase
import com.example.basicproject.user.data.local.repository.UserRepository
import com.example.basicproject.user.domain.usecase.LogoutUseCase
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

    @Provides
    fun provideLoginUseCase(
        loginRepository: LoginRepository,
        userRepository: UserRepository,
        sessionManager: SessionManager
    ): LoginUseCase = LoginUseCase(loginRepository, userRepository, sessionManager)

    @Provides
    fun provideGetProductsUseCase(repository: HomeRepository): GetProductsUseCase =
        GetProductsUseCase(repository)

    @Provides
    fun provideLogoutUseCase(
        userRepository: UserRepository,
        sessionManager: SessionManager
    ): LogoutUseCase = LogoutUseCase(userRepository, sessionManager)

}