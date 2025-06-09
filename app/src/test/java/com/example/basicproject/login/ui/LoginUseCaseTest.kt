package com.example.basicproject.login.ui

import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.login.data.TestLoginResponseFactory
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.login.domain.usecase.LoginUseCase
import com.example.basicproject.user.data.local.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    private val loginRepository = mockk<LoginRepository>()
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val sessionManager = mockk<SessionManager>(relaxed = true)

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        loginUseCase = LoginUseCase(loginRepository, userRepository, sessionManager)
    }

    @Test
    fun `should return success and save user and session`() = runTest {
        val response = TestLoginResponseFactory.create()
        coEvery { loginRepository.login("bruno", "123") } returns LoginResult.Success(response)

        val result = loginUseCase("bruno", "123")

        assert(result is LoginResult.Success)
        coVerify { userRepository.saveUser(response.toEntity()) }
        coVerify { sessionManager.saveSession("token123") }
    }

    @Test
    fun `should return error when repository fails`() = runTest {
        coEvery { loginRepository.login(any(), any()) } returns LoginResult.ServerError("Erro")

        val result = loginUseCase("x", "y")

        assert(result is LoginResult.ServerError)
    }
}