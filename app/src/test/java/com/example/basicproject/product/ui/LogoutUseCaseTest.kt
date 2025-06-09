package com.example.basicproject.product.ui

import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.user.data.local.repository.UserRepository
import com.example.basicproject.user.domain.usecase.LogoutUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LogoutUseCaseTest {

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val sessionManager = mockk<SessionManager>(relaxed = true)
    private lateinit var useCase: LogoutUseCase

    @Before
    fun setup() {
        useCase = LogoutUseCase(userRepository, sessionManager)
    }

    @Test
    fun `should clear user and session`() = runTest {
        useCase()

        coVerify { userRepository.clearUser() }
        coVerify { sessionManager.clearSession() }
    }
}