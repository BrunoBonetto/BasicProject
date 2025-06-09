package com.example.basicproject.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.home.domain.result.ListProductsResult
import com.example.basicproject.home.domain.usecase.GetProductsUseCase
import com.example.basicproject.home.ui.state.HomeIntent
import com.example.basicproject.home.ui.state.HomeUiState
import com.example.basicproject.home.ui.state.reduceResult
import com.example.basicproject.user.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent: SharedFlow<HomeUiEvent> = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadProducts -> loadProducts()
            is HomeIntent.Logout -> logout()
        }
    }

    private fun loadProducts() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = getProductsUseCase()) {
                is ListProductsResult.Success -> {
                    _uiState.value = reduceResult(_uiState.value, result)
                }

                is ListProductsResult.ServerError -> {
                    _uiState.value = reduceResult(_uiState.value, result)
                }
            }
        }
    }

        private fun logout() {
            viewModelScope.launch {
                try {
                    logoutUseCase()
                    _uiEvent.emit(HomeUiEvent.LogoutSuccess)
                } catch (e: Exception) {
                    _uiEvent.emit(HomeUiEvent.LogoutError)
                }
            }
        }

    }