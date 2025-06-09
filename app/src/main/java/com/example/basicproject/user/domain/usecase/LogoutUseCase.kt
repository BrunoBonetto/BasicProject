package com.example.basicproject.user.domain.usecase

import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.user.data.local.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke() {
        userRepository.clearUser()
        sessionManager.clearSession()
    }
}