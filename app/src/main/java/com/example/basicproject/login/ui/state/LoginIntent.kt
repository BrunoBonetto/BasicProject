package com.example.basicproject.login.ui.state

sealed class LoginIntent {
    object Submit : LoginIntent()
    data class UserNameChanged(val value: String) : LoginIntent()
    data class PasswordChanged(val value: String) : LoginIntent()
}