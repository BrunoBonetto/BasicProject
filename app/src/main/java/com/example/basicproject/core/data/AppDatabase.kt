package com.example.basicproject.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basicproject.user.data.local.dao.UserDao
import com.example.basicproject.user.data.remote.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}