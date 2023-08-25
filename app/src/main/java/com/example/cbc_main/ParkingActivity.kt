package com.example.cbc_main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cbc_main.databinding.ActivityParkingBinding
import com.example.presstest.PressureMsg
import com.example.presstest.PressureService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ParkingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityParkingBinding

    private val pbWheel: ProgressBar by lazy { findViewById(R.id.pbWheel) }

    private val retrofit = Retrofit.Builder().baseUrl("http://parkbomin.iptime.org:18000/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val api = ApiClient.getApiClient().create(PressureService::class.java)

    private val service = retrofit.create(PressureService::class.java)





    private var isChecked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        showPressure()




    }

    override fun onDestroy() {
        resetPressure()
        super.onDestroy()
    }




    @SuppressLint("ResourceAsColor")
    private fun imgParkingOn(){
        binding.parkingO.visibility = View.VISIBLE
        binding.parkingX.visibility = View.GONE
        binding.tvN.setTextColor(Color.parseColor("#A6A6A6"))
        binding.tvP.setTextColor(Color.parseColor("#3D5220"))
        binding.tvON.visibility = View.VISIBLE
        binding.tvOff.visibility = View.GONE
        binding.tvStableOn.visibility = View.VISIBLE
        binding.tvStableOff.visibility = View.GONE
    }
    @SuppressLint("ResourceAsColor")
    private fun imgParkingOff(){
        binding.parkingO.visibility = View.GONE
        binding.parkingX.visibility = View.VISIBLE
        binding.tvN.setTextColor(Color.parseColor("#FF0000"))
        binding.tvP.setTextColor(Color.parseColor("#A6A6A6"))
        binding.tvON.visibility = View.GONE
        binding.tvOff.visibility = View.VISIBLE
        binding.tvStableOn.visibility = View.GONE
        binding.tvStableOff.visibility = View.VISIBLE
    }





    private val mDelayHandler: Handler by lazy {
        Handler()
    }

    private fun waitPressure(){
        mDelayHandler.postDelayed(::showPressure, 1000) // 10초 후에 showGuest 함수를 실행한다.
    }

    private fun resetPressure(){
        mDelayHandler.removeMessages(0)
    }




    private fun showPressure(){


        service.getPressure().enqueue(object : Callback<PressureMsg> {
            @SuppressLint("ResourceAsColor")
            override fun onResponse(call: Call<PressureMsg>, response: Response<PressureMsg>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성고된 경우
                    var result = response.body()


                    if(result?.MESSAGE?.get(0)!!.pressure.toInt() > 0){
                        pbWheel.progress = result?.MESSAGE?.get(0)!!.pressure.toInt()
                        binding.tvTempF.text = result?.MESSAGE?.get(0)!!.tem.toInt().toString() + "°"
                        binding.tvHumF.text = result?.MESSAGE?.get(0)!!.humi.toInt().toString() + "%"
                        imgParkingOn()
                        if(result?.MESSAGE?.get(0)!!.pressure.toInt() <= 100){
                            binding.tvStableOn.setText("점검필요")
                            pbWheel.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        }else if (result?.MESSAGE?.get(0)!!.pressure.toInt() > 100 && result?.MESSAGE?.get(0)!!.pressure.toInt() <= 800){
                            binding.tvStableOn.setText("공기압안정적")
                            pbWheel.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#B7CB99")));

                        }else if (result?.MESSAGE?.get(0)!!.pressure.toInt() > 800 && result?.MESSAGE?.get(0)!!.pressure.toInt() < 1000){
                            binding.tvStableOn.setText("점검필요")
                            pbWheel.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        }
                    }else{
                        pbWheel.progress = 0
                        imgParkingOff()
                    }


                    Log.d("YMC", "압력: " + result?.MESSAGE?.get(0)?.pressure?.toInt());
                    Log.d("YMC", "습도: " + result?.MESSAGE?.get(0)?.humi?.toInt());
                    Log.d("YMC", "온도: " + result?.MESSAGE?.get(0)?.tem?.toInt());
                }else{
                    Toast.makeText(this@ParkingActivity, "연결상태를 확인해주세요", Toast.LENGTH_LONG).show()
                    Log.d("YMC", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<PressureMsg>, t: Throwable) {
                if(isChecked){
                    Toast.makeText(this@ParkingActivity, "자전거 거치대 연결상태를 확인하세요", Toast.LENGTH_LONG ).show()
                    isChecked = false
                }

                imgParkingOff()
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }
        })

        waitPressure() // 코드 실행뒤에 계속해서 반복하도록 작업한다.
    }






}






