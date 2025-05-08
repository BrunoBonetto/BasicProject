package com.example.basicproject.user.data.remote.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val userName: String,
    val email: String
)
