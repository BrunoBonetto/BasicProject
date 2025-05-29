package com.example.basicproject.home.domain.result

import com.example.basicproject.home.data.remote.model.ProductEntity

sealed class ListProductsResult {
    data class Success(val products: List<ProductEntity>) : ListProductsResult()
    data class ServerError(val error: String) : ListProductsResult()
}