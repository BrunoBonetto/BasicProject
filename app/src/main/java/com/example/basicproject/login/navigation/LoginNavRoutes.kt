package com.example.basicproject.login.navigation

sealed class LoginNavRoutes(val route: String) {
    object Login : LoginNavRoutes("login")
}