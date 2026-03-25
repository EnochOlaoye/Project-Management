package com.example.solutions4u.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Sets up the connection to our backend API server.
// Uses 10.0.2.2 which is how the Android emulator reaches the host machine's localhost.
// The API service is created once and reused across the whole app.
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
