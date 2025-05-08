package com.example.basicproject.home.navigation

sealed class HomeNavRoutes(val route: String) {
    object Home: HomeNavRoutes("home")
}