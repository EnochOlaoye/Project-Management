package com.example.solutions4u.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path

// The data we send to the server when someone registers a new account
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// The data we send to the server when someone tries to log in
data class LoginRequest(
    val email: String,
    val password: String
)

// The response we get back from the server after a login or register attempt.
// If something went wrong, the error field will have a message.
// If login was successful, user and token will be filled in.
data class AuthResponse(
    val message: String,
    val token: String?,
    val user: UserData?,
    val error: String?
)

// Holds the basic info about a logged-in user
data class UserData(
    val id: Int,
    val name: String,
    val email: String
)

// Defines the API endpoints that our app can call on the backend server
interface ApiService {

    // Send a register request and get back a response
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    // Send a login request and get back a response with the user's details
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @DELETE("users/{id}")
    suspend fun deleteAccount(@Path("id") userId: String): AuthResponse
}
