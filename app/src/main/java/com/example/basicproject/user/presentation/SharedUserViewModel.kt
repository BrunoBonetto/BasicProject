package com.example.basicproject.user.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.basicproject.user.data.remote.entity.UserEntity
import com.example.basicproject.user.domain.model.UserState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedUserViewModel @Inject constructor() : ViewModel() {
    var userState by mutableStateOf<UserState>(UserState.NotLoggedIn)
        private set

    fun setUser(user: UserEntity) {
        userState = UserState.LoggedIn(user)
    }

    fun clearUser(){
        userState = UserState.NotLoggedIn
    }
}