package com.example.cbc_main


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface LocationService {
    @POST("/sensor/phone/")
    @Headers("accept: application/json", "content-type: application/json")
    fun locationPost(
        @Header("Authorization") access_token : String,
        @Body locationTest : LoacationData
    ) : Call<LoacationData>
}