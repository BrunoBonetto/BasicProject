package com.example.basicproject.login.data.remote.model

import com.example.basicproject.user.data.remote.entity.UserEntity
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val id: Int,
    @SerializedName("username")
    val userName: String,
    val email: String,
    val token: String
)

fun LoginResponse.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        userName = this.userName,
        email = this.email
    )
}