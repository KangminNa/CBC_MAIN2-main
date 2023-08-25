package com.example.cbc_main.`interface`

import com.example.cbc_main.data.ShockRequest
import com.example.cbc_main.data.ShockResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ShockService {
    @POST("/sensor/count/")
    @Headers("accept: application/json", "content-type: application/json")
    fun postShock(
        @Header("Authorization") access_token : String,
        @Body shockRequest: ShockRequest
    ): Call<ShockResponse>
}