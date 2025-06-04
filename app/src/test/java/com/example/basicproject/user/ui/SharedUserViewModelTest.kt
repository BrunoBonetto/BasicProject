package com.example.basicproject.user.ui

import app.cash.turbine.test
import com.example.basicproject.login.data.TestLoginResponseFactory
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.user.ui.state.CurrentUserState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SharedUserViewModelTest {

    private val viewModel = SharedUserViewModel()

    @Test
    fun `initial state should be Unloaded`() = runTest {
        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Unloaded, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setUser should emit Success state with user`() = runTest {
        val user = TestLoginResponseFactory.create().toEntity()
        viewModel.setUser(user)

        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Success(user), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearUser should reset state to Unloaded`() = runTest {
        val user = TestLoginResponseFactory.create().toEntity()
        viewModel.setUser(user)
        viewModel.clearUser()

        viewModel.currentUserState.test {
            assertEquals(CurrentUserState.Unloaded, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}