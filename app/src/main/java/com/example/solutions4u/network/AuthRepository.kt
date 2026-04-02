package com.example.solutions4u.network

// This class handles the actual logic of calling the API for login and registration.
// It wraps the raw API calls and returns a simple Success or Error result
// so the screens don't have to deal with network details.

import org.json.JSONObject

// The result of an authentication attempt - either it worked or it didn't
sealed class AuthResult {
    data class Success(val message: String, val token: String?, val user: UserData?) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository {
    private val api = RetrofitClient.apiService

    // Try to register a new user. Returns Success if it worked, or Error with a message if not.
    suspend fun register(name: String, email: String, password: String): AuthResult {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                val body = response.body()
                AuthResult.Success(body?.message ?: "Registration successful", null, null)
            } else {
                val errorMessage = extractError(response.errorBody()?.string())
                AuthResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            AuthResult.Error("Could not connect to server. Is it running?")
        }
    }

    // Try to log a user in. Returns Success with their details if it worked, or Error if not.
    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                AuthResult.Success(
                    body?.message ?: "Login successful",
                    body?.token,
                    body?.user
                )
            } else {
                val errorMessage = extractError(response.errorBody()?.string())
                AuthResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            AuthResult.Error("Could not connect to server. Is it running?")
        }
    }

    // Delete user account from database.
    suspend fun deleteAccount(userId: String): Boolean {
        return try {
            val response = api.deleteAccount(userId)
            response.error == null
        } catch (e: Exception) {
            false
        }
    }
    // Helper function to extract "error" from backend JSON
    private fun extractError(errorBody: String?): String {
        return try {
            JSONObject(errorBody ?: "").getString("error")
        } catch (e: Exception) {
            "Something went wrong"
        }
    }
}
