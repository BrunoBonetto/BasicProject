package com.example.basicproject.home.data.remote.api

import com.example.basicproject.home.data.remote.model.ProductEntity
import com.example.basicproject.home.data.remote.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApiService {
    @GET("products")
    suspend fun getListProducts(): Response<ProductResponse>

    @GET("products/{productId}")
    suspend fun getProduct(@Path("productId") productId: Int): Response<ProductEntity>
}