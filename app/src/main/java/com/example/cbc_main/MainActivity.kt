package com.example.cbc_main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cbc_main.Detail.DetailActivity
import com.example.cbc_main.data.WeatherResponse
import com.example.cbc_main.databinding.ActivityMainBinding
import com.example.cbc_main.retrofit.RetrofitClass
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val btnRidingStart: Button by lazy { findViewById(R.id.btnRidingStart) }
    private val todayMMDD : TextView by lazy { findViewById(R.id.todayMMDD) }

    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        today()

        var imgurl: String = ""

        //날씨 보이기
        val service = RetrofitClass.weatherService


        val callGetW = service.getCurrentWeatherData("37", "126","1681d3f8a577fed2061198d014e9d4eb")

        var Celsius:Double = 0.0
        var CurrentWeather: String = ""
        callGetW.enqueue(object : retrofit2.Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                response.body().let {
                    if(it?.main?.temp != null){
                        Celsius = (it.main.temp)
                    }
                    if(it?.weather?.get(0)?.main != null) {
                        CurrentWeather = it.weather.get(0).main.toString()
                        imgurl = setImg(CurrentWeather)
                        //날씨 이미지
                        Glide.with(this@MainActivity)
                            .load(imgurl)
                            .into(binding.imgWeather)
                    }
                    binding.tvTemp2.text = (Celsius - 273).roundToInt().toString() + "°"
                    binding.tvHum2.text = it?.main?.humidity.toString() + "%"

                    binding.pbWeather.progress =  (Celsius - 273).roundToInt()
                    binding.pbTemp.progress = (Celsius - 273).roundToInt()
                    binding.pbHum.progress =  it?.main?.humidity!!.toInt()
                }

                Log.d("Weather", response.body().toString())
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("Weather", t.toString())
            }
        })

        binding.mainParking.setOnClickListener {

            val intent = Intent(this@MainActivity, ParkingActivity::class.java)
            startActivity(intent)
        }

        binding.mainMap.setOnClickListener {

            val intent = Intent(this@MainActivity, ThemeMapActivity::class.java)
            startActivity(intent)
        }

        binding.mainThemeMap.setOnClickListener {

            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent)
        }

        binding.mainRepair.setOnClickListener {

            val intent = Intent(this@MainActivity, RepairTipActivity::class.java)
            startActivity(intent)
        }


        binding.mainRecord.setOnClickListener {

            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            startActivity(intent)
        }

        btnRidingStart.setOnClickListener {
            val intent = Intent(this@MainActivity, RidingActivity::class.java)
            startActivity(intent)
        }

    }

    private fun today(){
        val today : Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("MM월 dd일")
        todayMMDD.text = sdf.format(today)
    }

    fun setImg(w: String): String {
        when(w) {
            "ThunderStorm" -> return "http://openweathermap.org/img/wn/11d@2x.png"
            "Drizzle" -> return "http://openweathermap.org/img/wn/09d@2x.png"
            "Rain" -> return "http://openweathermap.org/img/wn/10d@2x.png"
            "Snow" -> return "http://openweathermap.org/img/wn/13d@2x.png"
            "Atmosphere" -> return "http://openweathermap.org/img/wn/50d@2x.png"
            "Clouds" -> return "http://openweathermap.org/img/wn/02d@2x.png"
            else -> return "http://openweathermap.org/img/wn/01d@2x.png"
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }


}



