package com.example.basicproject.main.navigations

sealed class SessionNavRoutes(val route: String) {
    object Root : SessionNavRoutes("session_root")
}