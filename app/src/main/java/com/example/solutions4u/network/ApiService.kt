package com.example.solutions4u.network

// Defines what API calls exist

import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val message: String,
    val token: String?,
    val user: UserData?,
    val error: String?
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String
)

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
}