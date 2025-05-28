package com.example.basicproject.home.ui

import app.cash.turbine.test
import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.home.data.TestProductFactory
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ListProductsResult
import com.example.basicproject.home.ui.state.HomeIntent
import com.example.basicproject.user.data.local.repository.UserRepository
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val homeRepository: HomeRepository = mockk()
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = HomeViewModel(homeRepository, userRepository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when LoadProducts is triggered, uiState should contain product list`() = runTest {
        val fakeProducts = listOf(TestProductFactory.create())
        coEvery { homeRepository.getListProducts() } returns ListProductsResult.Success(fakeProducts)

        viewModel.onIntent(HomeIntent.LoadProducts)

        viewModel.uiState.test {
            val item = awaitItem()
            assertEquals(false, item.isLoading)
            assertEquals(fakeProducts, item.products)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when LoadProducts fails, uiState should contain error`() = runTest {
        val error = "Erro ao carregar"
        coEvery { homeRepository.getListProducts() } returns ListProductsResult.ServerError(error)

        viewModel.onIntent(HomeIntent.LoadProducts)

        viewModel.uiState.test {
            val item = awaitItem()
            assertEquals(false, item.isLoading)
            assertEquals(error, item.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when Logout is successful, emits LogoutSuccess`() = runTest {
        coEvery { userRepository.clearUser() } just runs
        coEvery { sessionManager.clearSession() } just runs

        viewModel.uiEvent.test {
            viewModel.onIntent(HomeIntent.Logout)
            assertEquals(HomeUiEvent.LogoutSuccess, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when Logout throws exception, emits LogoutError`() = runTest {
        coEvery { userRepository.clearUser() } throws RuntimeException("Erro")

        viewModel.onIntent(HomeIntent.Logout)

        viewModel.uiEvent.test {
            assertEquals(HomeUiEvent.LogoutError, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}
