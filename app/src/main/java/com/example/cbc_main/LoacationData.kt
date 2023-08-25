package com.example.cbc_main

import com.google.gson.annotations.SerializedName

data class LoacationData(
    @SerializedName("lat") var lat : Float,
    @SerializedName("lng") var lng: Float
)
