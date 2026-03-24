package com.example.solutions4u.network

// Handles the logic of calling the Api

sealed class AuthResult {
    data class Success(val message: String, val token: String?, val user: UserData?) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository {
    private val api = RetrofitClient.apiService

    suspend fun register(name: String, email: String, password: String): AuthResult {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            if (response.error != null) {
                AuthResult.Error(response.error)
            } else {
                AuthResult.Success(response.message, null, null)
            }
        } catch (e: Exception) {
            AuthResult.Error("Could not connect to server. Is it running?")
        }
    }

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.error != null) {
                AuthResult.Error(response.error)
            } else {
                AuthResult.Success(response.message, response.token, response.user)
            }
        } catch (e: Exception) {
            AuthResult.Error("Could not connect to server. Is it running?")
        }
    }
}