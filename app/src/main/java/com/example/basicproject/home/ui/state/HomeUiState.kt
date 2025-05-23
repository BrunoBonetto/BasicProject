package com.example.basicproject.home.ui.state

import com.example.basicproject.home.data.remote.model.ProductEntity

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val products: List<ProductEntity> = emptyList()
)