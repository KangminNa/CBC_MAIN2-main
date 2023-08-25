package com.example.cbc_main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cbc_main.data.LoginRequest
import com.example.cbc_main.data.LoginResponse
import com.example.cbc_main.databinding.ActivityLogin1Binding
import com.example.cbc_main.retrofit.RetrofitClass
import retrofit2.Call
import retrofit2.Response

class LoginActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityLogin1Binding

    private val btnLogin: ImageView by lazy { findViewById(R.id.imgBtnLoginMove) }
    private val btnSignup: ImageView by lazy { findViewById(R.id.imgSignUpMove) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        btnLogin.setOnClickListener {

            val service = RetrofitClass.loginService

            val loginRequest = LoginRequest(binding.etEmail.text.toString(), binding.etPW.text.toString())

            val POSTL = service.postlogin(loginRequest)

            Log.d("Login", loginRequest.toString())

            POSTL.enqueue(object: retrofit2.Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    Log.d("Login", "통신 성공")
                    if(response.isSuccessful){
                        Log.d("Login", "로그인 성공")
                        Log.d("Login", response.body()?.token.toString())

                        MyApplication.prefs.setString("token", response.body()?.token.toString())

                        val intentLogin = Intent(this@LoginActivity1, MainActivity::class.java)
                        startActivity(intentLogin)
                        finish()
                    } else {
                        Log.d("Login", response.code().toString()+"로그인 실패")
                        Toast.makeText(this@LoginActivity1, setMessage(response.code().toString()), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Login", t.localizedMessage)
                }
            })
        }
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity1::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun setMessage(w: String): String {
        when(w) {
            "401" -> return "잘못된 비밀번호입니다."
            "402" -> return "존재하지 않는 이메일입니다."
            else -> return "로그인 할 수 없습니다."
        }
    }


}