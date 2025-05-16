package com.example.basicproject.splash.navigation

sealed class SplashNavRoutes(val route: String) {
    object Splash : SplashNavRoutes("splash")
}