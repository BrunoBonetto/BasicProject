package com.example.basicproject.login.ui

sealed class LoginUiEvent {
    object ShowInvalidCredentials : LoginUiEvent()
    data class ShowServerError(val rawMessage: String?) : LoginUiEvent()
    object ShowEmptyResponse : LoginUiEvent()
    object LoginSuccess : LoginUiEvent()
    object ShowLocalStorageError : LoginUiEvent()
}