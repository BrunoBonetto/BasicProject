package com.example.basicproject.core.session.domain.usecase

import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.core.session.domain.result.SessionValidationResult
import com.example.basicproject.login.domain.repository.LoginRepository
import javax.inject.Inject

class RevalidateSessionUseCase @Inject constructor(
    private val sessionManager: SessionManager,
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(): SessionValidationResult {
        val token = sessionManager.getAuthToken()

        if (token.isNullOrBlank()) {
            return SessionValidationResult.InvalidToken
        }

        return try {
            when(val user = loginRepository.getUserFromToken(token)){
                is SessionValidationResult.Success -> user
                is SessionValidationResult.InvalidToken -> SessionValidationResult.InvalidToken
                is SessionValidationResult.ServerError -> SessionValidationResult.ServerError(user.error)
            }
        } catch (e: Exception) {
            SessionValidationResult.ServerError(e.message ?: "Unknown error")
        }
    }
}