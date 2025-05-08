package com.example.basicproject.core.auth.data.local

interface AuthLocalDataSource {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}