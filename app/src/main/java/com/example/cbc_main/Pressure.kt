package com.example.presstest

import com.google.gson.annotations.SerializedName

data class Pressure(
    @SerializedName("pressure") var pressure : Float,
    @SerializedName("tem") var tem : Float,
    @SerializedName("humi") var humi : Float

)
data class PressureMsg(
    @SerializedName("MESSAGE") var MESSAGE: List<Pressure>
)