package com.example.basicproject.login.ui.state

import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.user.ui.state.CurrentUserState

fun reduceIntent(state: LoginUiState, intent: LoginIntent): LoginUiState {
    return when (intent) {
        is LoginIntent.UserNameChanged -> state.copy(userName = intent.value)
        is LoginIntent.PasswordChanged -> state.copy(password = intent.value)
        is LoginIntent.Submit -> state.copy(isLoading = true, errorMessage = null)
    }
}

fun reduceResult(state: LoginUiState, result: LoginResult): LoginUiState {
    return when (result) {
        is LoginResult.Success -> state.copy(isLoading = false, errorMessage = null)
        is LoginResult.InvalidCredentials -> state.copy(
            isLoading = false,
            errorMessage = null
        )

        is LoginResult.ServerError -> state.copy(isLoading = false, errorMessage = result.error)
        is LoginResult.EmptyResponse -> state.copy(
            isLoading = false,
            errorMessage = null
        )
    }
}

fun reduceUserState(
    result: LoginResult
): CurrentUserState {
    return when (result) {
        is LoginResult.Success -> CurrentUserState.Success(result.user.toEntity())
        is LoginResult.InvalidCredentials,
        is LoginResult.EmptyResponse,
        is LoginResult.ServerError -> CurrentUserState.Error
    }
}