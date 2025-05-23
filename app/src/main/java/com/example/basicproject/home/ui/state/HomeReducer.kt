package com.example.basicproject.home.ui.state

import com.example.basicproject.home.domain.result.ProductResult

fun reduceResult(state: HomeUiState, result: ProductResult): HomeUiState {
    return when (result) {
        is ProductResult.Success -> state.copy(
            isLoading = false,
            errorMessage = null,
            result.products
        )

        is ProductResult.ServerError -> state.copy(
            isLoading = false,
            errorMessage = result.error,
            emptyList()
        )
    }
}