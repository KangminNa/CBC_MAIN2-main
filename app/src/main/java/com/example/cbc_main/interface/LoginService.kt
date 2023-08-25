package com.example.cbc_main.`interface`

import com.example.cbc_main.data.LoginRequest
import com.example.cbc_main.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    @POST("user/log-in/")
    fun postlogin(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>
}