package com.example.basicproject.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.login.domain.repository.LoginRepository
import com.example.basicproject.login.domain.result.LoginResult
import com.example.basicproject.login.data.remote.model.toEntity
import com.example.basicproject.login.ui.state.CurrentUserState
import com.example.basicproject.login.ui.state.LoginUiState
import com.example.basicproject.user.data.local.repository.UserRepository
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
    private val userRepository: UserRepository
): ViewModel() {

    private val _currentUserState = MutableStateFlow<CurrentUserState>(CurrentUserState.Idle)
    val currentUserState: StateFlow<CurrentUserState> = _currentUserState.asStateFlow()

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent.asSharedFlow()

    fun login(userName: String, password: String){
        _uiState.value = LoginUiState.Loading
        _currentUserState.value = CurrentUserState.Loading
        viewModelScope.launch {
            when (val result = repository.login(userName, password)) {
                is LoginResult.Success -> {
                    try{
                        userRepository.saveUser(result.user.toEntity())
                        _currentUserState.value = CurrentUserState.Success(result.user.toEntity())
                        _uiEvent.emit(LoginUiEvent.LoginSuccess)
                    }catch (e: Exception){
                        _uiState.value = LoginUiState.Idle
                        _currentUserState.value = CurrentUserState.Error
                        _uiEvent.emit(LoginUiEvent.ShowLocalStorageError)
                        // Not strictly necessary now, but included as a safeguard:
                        // if any code is added below this block in the future, this ensures it won't run after a failure.
                        return@launch
                    }
                }

                LoginResult.InvalidCredentials -> {
                    _uiState.value = LoginUiState.Idle
                    _uiEvent.emit(LoginUiEvent.ShowInvalidCredentials)
                }

                is LoginResult.ServerError -> {
                    _uiState.value = LoginUiState.Idle
                    _uiEvent.emit(LoginUiEvent.ShowServerError(result.error))
                }

                LoginResult.EmptyResponse -> {
                    _uiState.value = LoginUiState.Idle
                    _uiEvent.emit(LoginUiEvent.ShowEmptyResponse)
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

}