package com.example.basicproject.user.data.local.repository

import com.example.basicproject.user.data.local.dao.UserDao
import com.example.basicproject.user.data.remote.entity.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun saveUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun getLoggedUser() : UserEntity? = userDao.getLoggedUser()

    suspend fun clearUser() = userDao.clearUser()

    suspend fun deleteUser(user: UserEntity) = userDao.deletesUser(user)

}