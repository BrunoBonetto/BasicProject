package com.example.basicproject.product.ui

import com.example.basicproject.home.data.TestProductFactory
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ListProductsResult
import com.example.basicproject.home.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetProductsUseCaseTest {

    private val repository = mockk<HomeRepository>()
    private lateinit var useCase: GetProductsUseCase

    @Before
    fun setup() {
        useCase = GetProductsUseCase(repository)
    }

    @Test
    fun `should return product list successfully`() = runTest {
        val products = listOf(TestProductFactory.create())
        coEvery { repository.getListProducts() } returns ListProductsResult.Success(products)

        val result = useCase()

        assert(result is ListProductsResult.Success)
        assert((result as ListProductsResult.Success).products == products)
    }

    @Test
    fun `should return server error`() = runTest {
        coEvery { repository.getListProducts() } returns ListProductsResult.ServerError("Erro")

        val result = useCase()

        assert(result is ListProductsResult.ServerError)
    }
}