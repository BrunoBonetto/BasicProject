package com.example.basicproject.home.data.remote.repository

import com.example.basicproject.home.data.remote.api.HomeApiService
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ProductResult
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApiService
) : HomeRepository {

    override suspend fun getProducts(): ProductResult {
        return try {
            val response = api.getProducts()

            if (response.isSuccessful) {
                response.body()?.let {
                    ProductResult.Success(it.products)
                } ?: ProductResult.ServerError(response.message())
            } else {
                ProductResult.ServerError(response.message())
            }
        } catch (e: Exception) {
            ProductResult.ServerError(e.message.toString())
        }
    }
}