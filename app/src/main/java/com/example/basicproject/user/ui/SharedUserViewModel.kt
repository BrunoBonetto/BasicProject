package com.example.basicproject.user.ui

import androidx.lifecycle.ViewModel
import com.example.basicproject.user.data.remote.entity.UserEntity
import com.example.basicproject.user.ui.state.CurrentUserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedUserViewModel @Inject constructor() : ViewModel() {
    private val _currentUserState = MutableStateFlow<CurrentUserState>(CurrentUserState.Unloaded)
    val currentUserState = _currentUserState.asStateFlow()

    fun clearUser() {
        _currentUserState.value = CurrentUserState.Unloaded
    }

    fun setUser(user: UserEntity) {
        _currentUserState.value = CurrentUserState.Success(user)
    }
}