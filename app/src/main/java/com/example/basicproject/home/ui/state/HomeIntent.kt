package com.example.basicproject.home.ui.state

sealed class HomeIntent {
    object LoadProducts : HomeIntent()
    object Logout : HomeIntent()
}