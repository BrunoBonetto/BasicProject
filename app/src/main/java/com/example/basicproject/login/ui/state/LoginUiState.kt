package com.example.basicproject.login.ui.state

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
}