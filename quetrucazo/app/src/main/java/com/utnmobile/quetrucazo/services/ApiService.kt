package com.utnmobile.quetrucazo.services

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

const val baseUrl = "https://5ed6-190-247-139-97.ngrok-free.app"

interface ApiService {
    @POST("users/login")
    suspend fun loginUser(@Body credentials: Credentials): Response<AuthResponse>

    @POST("users")
    suspend fun registerUser(@Body credentials: Credentials): Response<AuthResponse>
}

data class Credentials(val username: String, val password: String)

data class User(val id: Int, val username: String)
data class Token(val token: String)
data class AuthResponse(val user: User, val token: Token)

data class ErrorResponse(val type: String)


fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun parseError(json: String?): ErrorResponse? {
    return try {
        Gson().fromJson(json, ErrorResponse::class.java)
    } catch (e: JsonSyntaxException) {
        null
    }
}