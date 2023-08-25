package com.example.cbc_main.retrofit

import com.example.cbc_main.`interface`.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClass {
    val BASE_URL = "http://parkbomin.iptime.org:18000/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    //signup
    val signupService: SignupService by lazy{
        retrofit.build().create(SignupService::class.java)
    }

    //login
    val loginService: LoginService by lazy{
        retrofit.build().create(LoginService::class.java)
    }

    val shockService: ShockService by lazy {
        retrofit.build().create(ShockService::class.java)
    }

    //날씨 API 추가
    private val retrofitWeather: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
    }

    private val _api = retrofit.build().create(locationTest::class.java)
    val api
        get() = _api

    val weatherService: WeatherService by lazy {
        retrofitWeather.build().create(WeatherService::class.java)
    }


}