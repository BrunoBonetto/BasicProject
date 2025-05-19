package com.example.basicproject.login.data

import com.example.basicproject.login.data.remote.model.LoginResponse

object TestLoginResponseFactory {
    fun create(
        id: Int = 1,
        name: String = "Bruno",
        email: String = "bruno@email.com",
        token: String = "token"
    ) = LoginResponse(id, name, email, token)
}