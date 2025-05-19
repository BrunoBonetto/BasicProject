package com.example.basicproject.core.session.domain.result

import com.example.basicproject.login.data.remote.model.LoginResponse

sealed class SessionValidationResult {
    data class Success(val user: LoginResponse) : SessionValidationResult()
    object InvalidToken : SessionValidationResult()
    data class ServerError(val error: String) : SessionValidationResult()
}