package com.example.basicproject.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.login.ui.state.LoginIntent
import com.example.basicproject.login.ui.state.LoginUiState
import com.example.basicproject.login.ui.state.reduceIntent
import com.example.basicproject.login.ui.state.reduceResult
import com.example.basicproject.login.ui.state.reduceUserState
import com.example.basicproject.user.data.local.repository.UserRepository
import com.example.basicproject.user.presentation.state.CurrentUserState
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

    fun login() {
        _currentUserState.value = CurrentUserState.Loading
        viewModelScope.launch {
            when (val result = repository.login(_uiState.value.userName, _uiState.value.password)) {
                is LoginResult.Success -> {
                    try {
                        userRepository.saveUser(result.user.toEntity())
                        sessionManager.saveSession(result.user.accessToken)
                        _currentUserState.value = reduceUserState(result)
                        _uiState.value =
                            reduceResult(_uiState.value, LoginResult.Success(result.user))
                        _uiEvent.emit(LoginUiEvent.LoginSuccess)
                    } catch (e: Exception) {
                        _currentUserState.value = CurrentUserState.Error
                        _uiState.value = reduceResult(
                            _uiState.value,
                            LoginResult.ServerError(e.message.toString())
                        )
                        _uiEvent.emit(LoginUiEvent.ShowLocalStorageError)
                        // Not strictly necessary now, but included as a safeguard:
                        // if any code is added below this block in the future, this ensures it won't run after a failure.
                        return@launch
                    }
                }

                LoginResult.InvalidCredentials -> {
                    _currentUserState.value = reduceUserState(result)
                    _uiState.value = reduceResult(_uiState.value, LoginResult.InvalidCredentials)
                    _uiEvent.emit(LoginUiEvent.ShowInvalidCredentials)
                }

                is LoginResult.ServerError -> {
                    _currentUserState.value = reduceUserState(result)
                    _uiState.value =
                        reduceResult(_uiState.value, LoginResult.ServerError(result.error))
                    _uiEvent.emit(LoginUiEvent.ShowServerError(result.error))
                }

                LoginResult.EmptyResponse -> {
                    _currentUserState.value = reduceUserState(result)
                    _uiState.value = reduceResult(_uiState.value, LoginResult.EmptyResponse)
                    _uiEvent.emit(LoginUiEvent.ShowEmptyResponse)
                }
            }
        }
    }
}