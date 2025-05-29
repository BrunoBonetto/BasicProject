package com.example.basicproject.product.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.core.utils.empty
import com.example.basicproject.home.domain.repository.HomeRepository
import com.example.basicproject.home.domain.result.ProductResult
import com.example.basicproject.product.ui.state.ProductDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId = savedStateHandle.get<Int>("productId") ?: -1

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun loadProduct() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = repository.getProduct(productId)) {
                is ProductResult.Success -> _uiState.value =
                    ProductDetailUiState(false, null, result.product)

                is ProductResult.ServerError -> _uiState.value =
                    ProductDetailUiState(false, result.error, null)
            }
        }
    }

}