package com.example.solutions4u.network

import org.json.JSONObject

// This class handles all property-related API calls.
// It wraps the raw API calls and returns a simple Success or Error result
// so the screens don't have to deal with network details.

// The result of a property operation - either it worked or it didn't
sealed class PropertyResult {
    data class Success(
        val properties: List<Property> = emptyList(),
        val property: Property? = null
    ) : PropertyResult()
    data class Error(val message: String) : PropertyResult()
}

class PropertyRepository {
    private val api = RetrofitClient.apiService

    // Fetch all properties for a given user.
    suspend fun getProperties(userId: Int): PropertyResult {
        return try {
            val response = api.getProperties(userId)
            if (response.isSuccessful) {
                PropertyResult.Success(properties = response.body()?.properties ?: emptyList())
            } else {
                PropertyResult.Error(extractError(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            PropertyResult.Error("Could not connect to server. Is it running?")
        }
    }

    // Add a new property for a given user. Returns the saved property (with its DB id) on success.
    suspend fun addProperty(
        userId: Int,
        name: String,
        addressLine1: String,
        addressLine2: String,
        eircode: String
    ): PropertyResult {
        return try {
            val response = api.addProperty(
                PropertyRequest(userId, name, addressLine1, addressLine2, eircode)
            )
            if (response.isSuccessful) {
                PropertyResult.Success(property = response.body()?.property)
            } else {
                PropertyResult.Error(extractError(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            PropertyResult.Error("Could not connect to server. Is it running?")
        }
    }

    // Delete a property by its ID.
    suspend fun deleteProperty(propertyId: Int): PropertyResult {
        return try {
            val response = api.deleteProperty(propertyId)
            if (response.isSuccessful) {
                PropertyResult.Success()
            } else {
                PropertyResult.Error(extractError(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            PropertyResult.Error("Could not connect to server. Is it running?")
        }
    }

    suspend fun updateProperty(
    propertyId: Int,
    name: String,
    addressLine1: String,
    addressLine2: String,
    eircode: String
): PropertyResult {
    return try {
        val response = api.updateProperty(
            propertyId,
            PropertyRequest(0, name, addressLine1, addressLine2, eircode)
        )
        if (response.isSuccessful) {
            PropertyResult.Success(property = response.body()?.property)
        } else {
            PropertyResult.Error(extractError(response.errorBody()?.string()))
        }
    } catch (e: Exception) {
        PropertyResult.Error("Could not connect to server. Is it running?")
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