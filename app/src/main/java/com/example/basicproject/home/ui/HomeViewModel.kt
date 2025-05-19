package com.example.basicproject.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.core.session.domain.SessionManager
import com.example.basicproject.user.presentation.state.CurrentUserState
import com.example.basicproject.user.data.local.repository.UserRepository
import com.example.basicproject.user.data.remote.entity.UserEntity
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
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _currentUserState = MutableStateFlow<CurrentUserState>(CurrentUserState.Unloaded)
    val currentUserState: StateFlow<CurrentUserState> = _currentUserState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent: SharedFlow<HomeUiEvent> = _uiEvent.asSharedFlow()

    fun logout(){
        viewModelScope.launch {
            try {
                userRepository.clearUser()
                sessionManager.clearSession()
                _currentUserState.value = CurrentUserState.Unloaded
                _uiEvent.emit(HomeUiEvent.LogoutSuccess)
            }catch (e: Exception){
                _currentUserState.value = CurrentUserState.Error
                _uiEvent.emit(HomeUiEvent.LogoutError)
            }
        }
    }

    fun setUser(user: UserEntity) {
        _currentUserState.value = CurrentUserState.Success(user)
    }

}