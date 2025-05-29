package com.example.basicproject.home.domain.result

import com.example.basicproject.home.data.remote.model.ProductEntity

sealed class ProductResult {
    data class Success(val product: ProductEntity) : ProductResult()
    data class ServerError(val error: String) : ProductResult()
}