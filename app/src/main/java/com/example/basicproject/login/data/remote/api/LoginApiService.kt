package com.example.basicproject.login.data.remote.api

import com.example.basicproject.login.data.remote.model.LoginRequest
import com.example.basicproject.login.data.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("auth/me")
    suspend fun getUserSession(@Header("Authorization") authHeader: String): Response<LoginResponse>
}