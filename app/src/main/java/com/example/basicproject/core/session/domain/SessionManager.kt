package com.example.basicproject.core.session.domain

interface SessionManager {
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getAuthToken(): String?
    suspend fun saveSession(token: String)
    suspend fun clearSession()
}