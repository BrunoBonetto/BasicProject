package com.example.basicproject.login.domain.result

import com.example.basicproject.login.data.remote.model.LoginResponse

sealed class LoginResult {
    data class Success(val user: LoginResponse) : LoginResult()
    object InvalidCredentials : LoginResult()
    data class ServerError(val error: String) : LoginResult()
    object EmptyResponse : LoginResult()
}