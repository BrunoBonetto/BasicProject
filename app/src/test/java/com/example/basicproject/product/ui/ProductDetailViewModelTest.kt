package com.example.basicproject.product.ui

import app.cash.turbine.test
import com.example.basicproject.home.data.TestProductFactory
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ProductResult
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
class ProductDetailViewModelTest {

    private val homeRepository: HomeRepository = mockk()
    private lateinit var viewModel: ProductDetailViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when loadProduct returns success, uiState should have product`() = runTest {
        val product = TestProductFactory.create()
        coEvery { homeRepository.getProduct(any()) } returns ProductResult.Success(product)

        viewModel = ProductDetailViewModel(homeRepository, createSavedStateHandle(1))
        viewModel.loadProduct()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(null, state.errorMessage)
            assertEquals(product, state.product)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when loadProduct returns error, uiState should have error message`() = runTest {
        val error = "Product not found"
        coEvery { homeRepository.getProduct(any()) } returns ProductResult.ServerError(error)

        viewModel = ProductDetailViewModel(homeRepository, createSavedStateHandle(999))
        viewModel.loadProduct()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(false, state.isLoading)
            assertEquals(error, state.errorMessage)
            assertEquals(null, state.product)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createSavedStateHandle(productId: Int) =
        androidx.lifecycle.SavedStateHandle(mapOf("productId" to productId))
}
