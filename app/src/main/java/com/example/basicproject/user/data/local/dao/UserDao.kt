package com.example.basicproject.user.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.basicproject.user.data.remote.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getLoggedUser(): UserEntity?

    @Query("DELETE FROM users")
    suspend fun clearUser()

    @Delete
    suspend fun deletesUser(user: UserEntity)

}