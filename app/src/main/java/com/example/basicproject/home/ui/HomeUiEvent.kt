package com.example.basicproject.home.ui

sealed class HomeUiEvent {
    object LogoutSuccess : HomeUiEvent()
    object LogoutError : HomeUiEvent()
}