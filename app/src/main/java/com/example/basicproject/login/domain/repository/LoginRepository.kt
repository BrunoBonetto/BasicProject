package com.example.basicproject.login.domain.repository

import com.example.basicproject.core.session.domain.result.SessionValidationResult
import com.example.basicproject.login.domain.result.LoginResult

interface LoginRepository {
    suspend fun login(username: String, password: String): LoginResult
    suspend fun getUserFromToken(token: String): SessionValidationResult
}