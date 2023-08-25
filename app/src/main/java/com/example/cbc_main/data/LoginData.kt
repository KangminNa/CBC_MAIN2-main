package com.example.cbc_main.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.Header

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("access_token") var token: String,
    @SerializedName("MESSAGE") var MESSAGE :String
)
