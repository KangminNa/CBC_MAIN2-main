package com.example.cbc_main.data

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("MESSAGE") var MESSAGE :String
)

data class SignUpRequest(
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("phone") var phone: String,
)
