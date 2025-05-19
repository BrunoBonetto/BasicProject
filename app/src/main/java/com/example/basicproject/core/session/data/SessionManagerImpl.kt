package com.example.basicproject.core.session.data

import com.example.basicproject.core.auth.data.local.AuthLocalDataSource
import com.example.basicproject.core.session.domain.SessionManager
import javax.inject.Inject

class SessionManagerImpl @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
): SessionManager {

    override suspend fun isUserLoggedIn(): Boolean {
        return !authLocalDataSource.getToken().isNullOrBlank()
    }

    override suspend fun getAuthToken(): String? {
        return authLocalDataSource.getToken()
    }

    override suspend fun saveSession(token: String) {
        authLocalDataSource.saveToken(token)
    }

    override suspend fun clearSession() {
        authLocalDataSource.clearToken()
    }

}