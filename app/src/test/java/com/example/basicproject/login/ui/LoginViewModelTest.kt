package com.example.basicproject.login.ui

import app.cash.turbine.test
import com.example.basicproject.core.rules.MainDispatcherRule
import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.login.data.TestLoginResponseFactory
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.login.ui.state.LoginIntent
import com.example.basicproject.login.ui.state.LoginUiState
import com.example.basicproject.login.ui.state.reduceUserState
import com.example.basicproject.user.data.local.repository.UserRepository
import com.example.basicproject.user.ui.state.CurrentUserState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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

    private val loginRepository: LoginRepository = mockk()
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    private lateinit var viewModel: LoginViewModel

    private fun assertUiState(
        expectedUserName: String? = null,
        expectedPassword: String? = null,
        expectedIsLoading: Boolean? = null,
        expectedErrorMessage: String? = null,
        state: LoginUiState = viewModel.uiState.value
    ) {
        expectedUserName?.let { assertEquals(it, state.userName) }
        expectedPassword?.let { assertEquals(it, state.password) }
        expectedIsLoading?.let { assertEquals(it, state.isLoading) }
        expectedErrorMessage?.let { assertEquals(it, state.errorMessage) }
    }

    @Before
    fun setup() {
        viewModel = LoginViewModel(loginRepository, userRepository, sessionManager)
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
    fun `when login is successful, emits LoginSuccess and updates uiState`() = runTest {
        val user = TestLoginResponseFactory.create()
        coEvery { loginRepository.login(any(), any()) } returns LoginResult.Success(user)

        viewModel.onIntent(LoginIntent.UserNameChanged("bruno"))
        viewModel.onIntent(LoginIntent.PasswordChanged("123456"))
        viewModel.onIntent(LoginIntent.Submit)

        viewModel.uiEvent.test {
            assertEquals(LoginUiEvent.LoginSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        assertUiState(
            expectedUserName = "bruno",
            expectedPassword = "123456",
            expectedIsLoading = false
        )
    }

    @Test
    fun `when login is successful, should save user, save session and emit CurrentUserState Success`() =
        runTest {
            val user = TestLoginResponseFactory.create()
            val userEntity = user.toEntity()

            coEvery { loginRepository.login(any(), any()) } returns LoginResult.Success(user)
            coEvery { userRepository.saveUser(userEntity) } just runs
            coEvery { sessionManager.saveSession(user.accessToken) } just runs

            viewModel.onIntent(LoginIntent.UserNameChanged("bruno"))
            viewModel.onIntent(LoginIntent.PasswordChanged("123456"))
            viewModel.onIntent(LoginIntent.Submit)

            viewModel.currentUserState.test {
                assertEquals(CurrentUserState.Loading, awaitItem())
                val result = awaitItem()
                assertTrue(result is CurrentUserState.Success)
                assertEquals(userEntity, (result as CurrentUserState.Success).user)
                cancelAndIgnoreRemainingEvents()
            }

            coVerify { userRepository.saveUser(userEntity) }
            coVerify { sessionManager.saveSession(user.accessToken) }

            assertUiState(
                expectedUserName = "bruno",
                expectedPassword = "123456",
                expectedIsLoading = false
            )
        }

    @Test
    fun `when login fails with invalid credentials, emits ShowInvalidCredentials and updates uiState`() =
        runTest {
            coEvery { loginRepository.login(any(), any()) } returns LoginResult.InvalidCredentials

            viewModel.onIntent(LoginIntent.Submit)

            viewModel.uiEvent.test {
                assertEquals(LoginUiEvent.ShowInvalidCredentials, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            viewModel.currentUserState.test {
                assertEquals(CurrentUserState.Error, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            assertUiState(
                expectedIsLoading = false
            )
        }

    @Test
    fun `when login fails with ServerError, emits ShowServerError and updates uiState`() = runTest {
        val errorMessage = "Server error"
        coEvery {
            loginRepository.login(
                any(),
                any()
            )
        } returns LoginResult.ServerError(errorMessage)

        viewModel.onIntent(LoginIntent.Submit)

        viewModel.uiEvent.test {
            assertEquals(LoginUiEvent.ShowServerError(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        assertUiState(
            expectedIsLoading = false,
            expectedErrorMessage = errorMessage
        )
    }

    @Test
    fun `when login fails with EmptyResponse, emits EmptyResponse and updates uiState`() = runTest {
        coEvery { loginRepository.login(any(), any()) } returns LoginResult.EmptyResponse

        viewModel.onIntent(LoginIntent.Submit)

        viewModel.uiEvent.test {
            assertEquals(LoginUiEvent.ShowEmptyResponse, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        assertUiState(
            expectedIsLoading = false
        )
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
