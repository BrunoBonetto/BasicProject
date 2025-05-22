package com.example.basicproject.login.ui.state

import com.example.basicproject.login.domain.result.LoginResult

fun reduceIntent(state: LoginUiState, intent: LoginIntent): LoginUiState {
    return when (intent) {
        is LoginIntent.UserNameChanged -> state.copy(userName = intent.value)
        is LoginIntent.PasswordChanged -> state.copy(password = intent.value)
        is LoginIntent.Submit -> state.copy(isLoading = true, errorMessage = null)
    }
}

fun reduceResult(state: LoginUiState, result: LoginResult): LoginUiState {
    return when (result) {
        is LoginResult.Success -> state.copy(isLoading = false)
        is LoginResult.InvalidCredentials -> state.copy(isLoading = false)
        is LoginResult.ServerError -> state.copy(isLoading = false, errorMessage = result.error)
        is LoginResult.EmptyResponse -> state.copy(isLoading = false)
    }
}