package com.example.basicproject.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.login.data.remote.model.LoginResponse
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.login.ui.state.LoginIntent
import com.example.basicproject.login.ui.state.LoginUiState
import com.example.basicproject.login.ui.state.reduceIntent
import com.example.basicproject.login.ui.state.reduceResult
import com.example.basicproject.login.ui.state.reduceUserState
import com.example.basicproject.user.data.local.repository.UserRepository
import com.example.basicproject.user.ui.state.CurrentUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _currentUserState = MutableStateFlow<CurrentUserState>(CurrentUserState.Unloaded)
    val currentUserState: StateFlow<CurrentUserState> = _currentUserState.asStateFlow()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Submit -> {
                reduceAndSetState(intent)
                login()
            }

            is LoginIntent.UserNameChanged,
            is LoginIntent.PasswordChanged -> {
                reduceAndSetState(intent)
            }
        }
    }

    private fun reduceAndSetState(intent: LoginIntent) {
        _uiState.value = reduceIntent(_uiState.value, intent)
    }

    private fun login() {
        _currentUserState.value = CurrentUserState.Loading
        viewModelScope.launch {
            when (val result = repository.login(_uiState.value.userName, _uiState.value.password)) {
                is LoginResult.Success -> {
                    try {
                        handleLoginSuccess(result.user)
                    } catch (e: Exception) {
                        handleLoginFailure(LoginResult.ServerError(e.message.toString()))
                        _uiEvent.emit(LoginUiEvent.ShowLocalStorageError)
                    }
                }

                else -> handleLoginFailure(result)
            }
        }
    }

    private suspend fun handleLoginSuccess(user: LoginResponse) {
        userRepository.saveUser(user.toEntity())
        sessionManager.saveSession(user.accessToken)
        _currentUserState.value = reduceUserState(LoginResult.Success(user))
        _uiState.value = reduceResult(_uiState.value, LoginResult.Success(user))
        _uiEvent.emit(LoginUiEvent.LoginSuccess)
    }


    private suspend fun handleLoginFailure(result: LoginResult) {
        _currentUserState.value = reduceUserState(result)
        _uiState.value = reduceResult(_uiState.value, result)
        val event = when (result) {
            LoginResult.InvalidCredentials -> LoginUiEvent.ShowInvalidCredentials
            is LoginResult.ServerError -> LoginUiEvent.ShowServerError(result.error)
            LoginResult.EmptyResponse -> LoginUiEvent.ShowEmptyResponse
            else -> return
        }
        _uiEvent.emit(event)
    }

}