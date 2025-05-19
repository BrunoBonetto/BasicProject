package com.example.basicproject.splash.ui

import app.cash.turbine.test
import com.example.basicproject.core.session.domain.result.SessionValidationResult
import com.example.basicproject.core.session.domain.usecase.RevalidateSessionUseCase
import com.example.basicproject.login.data.TestLoginResponseFactory
import com.example.basicproject.login.data.remote.model.LoginResponse
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.user.presentation.state.CurrentUserState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val revalidateSessionUseCase = mockk<RevalidateSessionUseCase>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `when session is valid, emits Success state and GoToHome event`() = runTest {

        val fakeLoginResponse = TestLoginResponseFactory.create()
        val fakeUser = fakeLoginResponse.toEntity()
        coEvery { revalidateSessionUseCase() } returns SessionValidationResult.Success(
            fakeLoginResponse
        )

        val viewModel = SplashViewModel(revalidateSessionUseCase)

        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Success(fakeUser), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiEvent.test {
            assertEquals(SplashUiEvent.GoToHome, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when session is invalid, emits Unloaded state and GoToLogin event`() = runTest {

        coEvery { revalidateSessionUseCase() } returns SessionValidationResult.InvalidToken

        val viewModel = SplashViewModel(revalidateSessionUseCase)

        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Unloaded, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiEvent.test {
            assertEquals(SplashUiEvent.GoToLogin, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when session validation fails, emits Error state and Error event with error message`() = runTest {

        val errorMessage = "Error message"
        coEvery { revalidateSessionUseCase() } returns  SessionValidationResult.ServerError(errorMessage)

        val viewModel = SplashViewModel(revalidateSessionUseCase)

        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiEvent.test {
            assertEquals(SplashUiEvent.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}