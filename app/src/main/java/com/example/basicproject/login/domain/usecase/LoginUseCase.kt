package com.example.basicproject.login.domain.usecase

import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.user.data.local.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(username: String, password: String): LoginResult {
        return when (val result = loginRepository.login(username, password)) {
            is LoginResult.Success -> {
                try {
                    userRepository.saveUser(result.user.toEntity())
                    sessionManager.saveSession(result.user.accessToken)
                    result
                } catch (e: Exception) {
                    LoginResult.ServerError(e.message.toString())
                }
            }

            else -> result
        }
    }
}