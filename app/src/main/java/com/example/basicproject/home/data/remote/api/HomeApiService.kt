package com.example.basicproject.home.data.remote.api

import com.example.basicproject.home.data.remote.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface HomeApiService {
    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>
}