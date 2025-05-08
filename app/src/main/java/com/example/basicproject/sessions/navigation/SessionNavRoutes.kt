package com.example.basicproject.sessions.navigation

sealed class SessionNavRoutes(val route: String) {
    object Root : SessionNavRoutes("session_root")
}