package com.example.basicproject.home.data.remote.repository

import com.example.basicproject.home.data.remote.api.HomeApiService
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ListProductsResult
import com.example.basicproject.home.domain.result.ProductResult
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApiService
) : HomeRepository {

    override suspend fun getListProducts(): ListProductsResult {
        return try {
            val response = api.getListProducts()

            if (response.isSuccessful) {
                response.body()?.let {
                    ListProductsResult.Success(it.products)
                } ?: ListProductsResult.ServerError(response.message())
            } else {
                ListProductsResult.ServerError(response.message())
            }
        } catch (e: Exception) {
            ListProductsResult.ServerError(e.message.toString())
        }
    }

    override suspend fun getProduct(productId: Int): ProductResult {
        return try {
            val response = api.getProduct(productId)

            if (response.isSuccessful) {
                response.body()?.let {
                    ProductResult.Success(it)
                } ?: ProductResult.ServerError(response.message())
            } else {
                ProductResult.ServerError(response.message())
            }
        } catch (e: Exception) {
            ProductResult.ServerError(e.message.toString())
        }
    }
}