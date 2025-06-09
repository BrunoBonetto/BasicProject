package com.example.basicproject.login.ui

import app.cash.turbine.test
import com.example.basicproject.core.rules.MainDispatcherRule
import com.example.basicproject.login.data.TestLoginResponseFactory
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.login.domain.usecase.LoginUseCase
import com.example.basicproject.login.ui.state.LoginIntent
import com.example.basicproject.login.ui.state.reduceUserState
import com.example.basicproject.user.ui.state.CurrentUserState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get: Rule
    val coroutineRule = MainDispatcherRule()

    private val loginUseCase = mockk<LoginUseCase>()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `when username is changed, state is updated`() {
        val newUsername = "bruno"
        viewModel.onIntent(LoginIntent.UserNameChanged(newUsername))
        assertEquals(newUsername, viewModel.uiState.value.userName)
    }

    @Test
    fun `when password is changed, state is updated`() {
        val newPassword = "123456"
        viewModel.onIntent(LoginIntent.PasswordChanged(newPassword))
        assertEquals(newPassword, viewModel.uiState.value.password)
    }


    @Test
    fun `should emit LoginSuccess on successful login`() = runTest {
        // arrange
        val response = TestLoginResponseFactory.create()
        coEvery { loginUseCase(any(), any()) } returns LoginResult.Success(response)

        // act
        viewModel.onIntent(LoginIntent.UserNameChanged("bruno"))
        viewModel.onIntent(LoginIntent.PasswordChanged("123"))
        viewModel.onIntent(LoginIntent.Submit)

        // assert
        viewModel.uiEvent.test {
            assert(awaitItem() == LoginUiEvent.LoginSuccess)
            cancelAndIgnoreRemainingEvents()
        }

        assert(viewModel.uiState.value.userName == "bruno")
        assert(viewModel.uiState.value.password == "123")
    }


    @Test
    fun `should emit ShowInvalidCredentials on failure`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns LoginResult.InvalidCredentials

        viewModel.onIntent(LoginIntent.Submit)

        viewModel.uiEvent.test {
            assert(awaitItem() == LoginUiEvent.ShowInvalidCredentials)
            cancelAndIgnoreRemainingEvents()
        }

        assert(viewModel.currentUserState.value is CurrentUserState.Error)
    }

    @Test
    fun `reduceUserState returns Success when LoginResult is Success`() {
        val user = TestLoginResponseFactory.create()
        val result = LoginResult.Success(user)

        val state = reduceUserState(result)

        assertTrue(state is CurrentUserState.Success)
        assertEquals(user.toEntity(), (state as CurrentUserState.Success).user)
    }

    @Test
    fun `reduceUserState returns Error when LoginResult is InvalidCredentials`() {
        val state = reduceUserState(LoginResult.InvalidCredentials)
        assertEquals(CurrentUserState.Error, state)
    }

    @Test
    fun `reduceUserState returns Error when LoginResult is ServerError`() {
        val state = reduceUserState(LoginResult.ServerError("Erro"))
        assertEquals(CurrentUserState.Error, state)
    }

    @Test
    fun `reduceUserState returns Error when LoginResult is EmptyResponse`() {
        val state = reduceUserState(LoginResult.EmptyResponse)
        assertEquals(CurrentUserState.Error, state)
    }

}
