package com.example.solutions4u.network

import retrofit2.http.*
import retrofit2.Response

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

// A single property belonging to a user
data class Property(
    val id: Int = 0,
    val userId: Int = 0,
    val name: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val eircode: String = ""
)

// Data we send when creating a new property
data class PropertyRequest(
    val userId: Int,
    val name: String,
    val addressLine1: String,
    val addressLine2: String,
    val eircode: String
)

// Response we get back from property endpoints
data class PropertyResponse(
    val message: String?,
    val property: Property?,
    val properties: List<Property>?,
    val error: String?
)

//Update user details
data class UpdateUserRequest(
    val name: String,
    val email: String,
    val password: String?
)
// Defines the API endpoints that our app can call on the backend server
interface ApiService {

    // Send a register request and get back a response
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    // Send a login request and get back a response with the user's details
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @DELETE("users/{id}")
    suspend fun deleteAccount(@Path("id") userId: String): Response<AuthResponse>

    //Get all properties belonging to a user
     @GET("users/{userId}/properties")
    suspend fun getProperties(@Path("userId") userId: Int): Response<PropertyResponse>

    // Create a new property for a user
    @POST("properties")
    suspend fun addProperty(@Body request: PropertyRequest): Response<PropertyResponse>
 
    // Delete a property by its ID
    @DELETE("properties/{propertyId}")
    suspend fun deleteProperty(@Path("propertyId") propertyId: Int): Response<PropertyResponse>

    @PUT("properties/{propertyId}")
    suspend fun updateProperty(@Path("propertyId") propertyId: Int, @Body request: PropertyRequest): Response<PropertyResponse>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") userId: String, @Body request: UpdateUserRequest): Response<AuthResponse>
}