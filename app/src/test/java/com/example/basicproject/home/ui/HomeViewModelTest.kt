package com.example.basicproject.home.ui

import app.cash.turbine.test
import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.home.data.TestProductFactory
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ListProductsResult
import com.example.basicproject.home.domain.usecase.GetProductsUseCase
import com.example.basicproject.home.ui.state.HomeIntent
import com.example.basicproject.user.data.local.repository.UserRepository
import com.example.basicproject.user.domain.usecase.LogoutUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getProductsUseCase = mockk<GetProductsUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(getProductsUseCase, logoutUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update state with products on success`() = runTest {
        val products = listOf(TestProductFactory.create())
        coEvery { getProductsUseCase() } returns ListProductsResult.Success(products)

        viewModel.onIntent(HomeIntent.LoadProducts)

        advanceUntilIdle()
        val state = viewModel.uiState.value

        assert(state.isLoading.not())
        assert(state.products == products)
    }

    @Test
    fun `should emit LogoutSuccess on logout`() = runTest {
        coEvery { logoutUseCase() } just Runs

        viewModel.onIntent(HomeIntent.Logout)

        viewModel.uiEvent.test {
            assert(awaitItem() == HomeUiEvent.LogoutSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit LogoutError on failure`() = runTest {
        coEvery { logoutUseCase() } throws Exception("Erro")

        viewModel.onIntent(HomeIntent.Logout)

        viewModel.uiEvent.test {
            assert(awaitItem() == HomeUiEvent.LogoutError)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
