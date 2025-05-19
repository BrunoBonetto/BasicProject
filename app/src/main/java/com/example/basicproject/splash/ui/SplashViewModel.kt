package com.example.basicproject.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.core.session.domain.result.SessionValidationResult
import com.example.basicproject.core.session.domain.usecase.RevalidateSessionUseCase
import com.example.basicproject.login.data.remote.model.toEntity
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
class SplashViewModel @Inject constructor(
    private val revalidateSessionUseCase: RevalidateSessionUseCase
) : ViewModel() {

    private val _currentUserState = MutableStateFlow<CurrentUserState>(CurrentUserState.Unloaded)
    val currentUserState: StateFlow<CurrentUserState> = _currentUserState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SplashUiEvent>(replay = 1)
    val uiEvent: SharedFlow<SplashUiEvent> = _uiEvent.asSharedFlow()

    init {
        validateSession()
    }

    private fun validateSession() {
        _currentUserState.value = CurrentUserState.Loading
        viewModelScope.launch {
            when (val result = revalidateSessionUseCase()) {
                is SessionValidationResult.Success -> {
                    _currentUserState.value = CurrentUserState.Success(result.user.toEntity())
                    _uiEvent.emit(SplashUiEvent.GoToHome)
                }
                is SessionValidationResult.ServerError -> {
                    _currentUserState.value = CurrentUserState.Error
                    _uiEvent.emit(SplashUiEvent.Error(result.error))
                }
                is SessionValidationResult.InvalidToken -> {
                    _currentUserState.value = CurrentUserState.Unloaded
                    _uiEvent.emit(SplashUiEvent.GoToLogin)
                }
            }
        }
    }
}