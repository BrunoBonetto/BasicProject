package com.example.basicproject.splash.ui

sealed class SplashUiEvent {
    object GoToLogin : SplashUiEvent()
    object GoToHome : SplashUiEvent()
    data class Error(val message: String) : SplashUiEvent()
}