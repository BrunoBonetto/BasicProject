package com.example.basicproject.login.ui.state

import com.example.basicproject.user.data.remote.entity.UserEntity

sealed class CurrentUserState {
    object Unloaded : CurrentUserState()
    object Loading : CurrentUserState()
    data class Success(val user: UserEntity) : CurrentUserState()
    object Error: CurrentUserState()
}