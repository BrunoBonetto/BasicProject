package com.example.basicproject.home.domain.repository

import com.example.basicproject.home.domain.result.ListProductsResult
import com.example.basicproject.home.domain.result.ProductResult

interface HomeRepository {
    suspend fun getListProducts(): ListProductsResult
    suspend fun getProduct(productId: Int): ProductResult
}