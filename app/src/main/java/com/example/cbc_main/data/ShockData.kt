package com.example.cbc_main.data

import com.google.gson.annotations.SerializedName

data class ShockRequest(
    @SerializedName("date") var date: String
)

data class ShockResponse(
    @SerializedName("MESSAGE") var MESSAGE: String
)
