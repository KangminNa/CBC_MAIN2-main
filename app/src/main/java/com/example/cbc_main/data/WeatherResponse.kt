package com.example.cbc_main.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather") var weather: ArrayList<Weather>,
    @SerializedName("main") var main: main
)

data class Weather(
    @SerializedName("id") var id: Int,
    @SerializedName("main") var main: String?,
    @SerializedName("description") var description: String?,
    @SerializedName("icon") var icon: String?
)

data class main(
    @SerializedName("temp") var temp:Double,
    @SerializedName("humidity") var humidity:Int
)
