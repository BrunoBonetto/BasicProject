package com.example.basicproject.login.ui.state

data  class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)