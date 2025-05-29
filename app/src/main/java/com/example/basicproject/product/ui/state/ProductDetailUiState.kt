package com.example.basicproject.product.ui.state

import com.example.basicproject.home.data.remote.model.ProductEntity

data class ProductDetailUiState (
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val product: ProductEntity? = null
)