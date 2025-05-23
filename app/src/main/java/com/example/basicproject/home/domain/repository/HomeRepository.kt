package com.example.basicproject.home.domain.repository

import com.example.basicproject.home.domain.result.ProductResult

interface HomeRepository {
    suspend fun getProducts(): ProductResult
}