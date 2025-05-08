package com.example.basicproject.login.domain.repository

import com.example.basicproject.login.domain.result.LoginResult

interface LoginRepository {
    suspend fun login(username: String, password: String): LoginResult
}