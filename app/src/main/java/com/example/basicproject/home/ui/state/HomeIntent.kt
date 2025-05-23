package com.example.basicproject.home.ui.state

import com.example.basicproject.user.data.remote.entity.UserEntity

sealed class HomeIntent {
    object LoadProducts : HomeIntent()
    object Logout : HomeIntent()
    data class SetUser(val user: UserEntity) : HomeIntent()
}