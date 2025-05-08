package com.example.basicproject.login.data.remote.repository

import com.example.basicproject.login.data.remote.model.LoginRequest
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.login.data.remote.api.LoginApiService
import com.example.basicproject.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApiService
) : LoginRepository {

    override suspend fun login(username: String, password: String): LoginResult {
        return try {
            val response = api.login(LoginRequest(username, password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                when {
                    loginResponse != null -> LoginResult.Success(loginResponse)
                    else -> LoginResult.EmptyResponse
                }
            } else {
                if (response.code() == 400) {
                    LoginResult.InvalidCredentials
                } else {
                    LoginResult.ServerError(response.message())
                }
            }

        }catch (e: Exception){
            LoginResult.ServerError(e.message.toString())
        }
    }
}