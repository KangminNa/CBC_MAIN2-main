package com.example.cbc_main.`interface`

import com.example.cbc_main.data.SignUpRequest
import com.example.cbc_main.data.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {
    @POST("user/sign-up/")
    fun postuser(
        @Body signUpRequest: SignUpRequest
    ):Call<SignUpResponse>
}