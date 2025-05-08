package com.example.basicproject.user.domain.model

import com.example.basicproject.user.data.remote.entity.UserEntity

sealed class UserState {
    object NotLoggedIn : UserState()
    data class LoggedIn(val user: UserEntity) : UserState()
}