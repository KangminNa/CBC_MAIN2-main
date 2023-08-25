package com.example.presstest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers


interface PressureService {
    @GET("sensor/holder/")
    fun getPressure(
    ):Call<PressureMsg>



}