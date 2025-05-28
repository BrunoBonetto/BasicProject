package com.example.basicproject.home.ui.state

import com.example.basicproject.home.domain.result.ListProductsResult

fun reduceResult(state: HomeUiState, result: ListProductsResult): HomeUiState {
    return when (result) {
        is ListProductsResult.Success -> state.copy(
            isLoading = false,
            errorMessage = null,
            result.products
        )

        is ListProductsResult.ServerError -> state.copy(
            isLoading = false,
            errorMessage = result.error,
            emptyList()
        )
    }
}