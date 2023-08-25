package com.example.cbc_main.`interface`

import com.example.cbc_main.data.testLocation
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface locationTest {
    @POST("sensor/phone/")
    @Headers("accept: application/json", "content-type: application/json")
    fun locationTest(
        @Header("Authorization") access_token : String,
        testLocation: testLocation
    ): Call<testLocation>
}