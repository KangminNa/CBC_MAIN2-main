package com.example.cbc_main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.cbc_main.data.SignUpRequest
import com.example.cbc_main.data.SignUpResponse
import com.example.cbc_main.databinding.ActivitySignUp1Binding
import com.example.cbc_main.retrofit.RetrofitClass
import retrofit2.Call
import retrofit2.Response

class SignUpActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp1Binding

    private val btnMoveLogin: Button by lazy { findViewById(R.id.btnSignUp) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.phonenum.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        btnMoveLogin.setOnClickListener {
            if(binding.password.text.toString() != binding.confirmpw.text.toString()) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

            val service = RetrofitClass.signupService

            val signUpRequest = SignUpRequest(binding.name.text.toString(), binding.email.text.toString()
                ,binding.password.text.toString(), binding.nickname.text.toString(), binding.phonenum.text.toString())

            val Postuser = service.postuser(signUpRequest)

            Postuser.enqueue(object: retrofit2.Callback<SignUpResponse> {
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    Log.d("Signup", "통신 성공")
                    if(response.isSuccessful) {
                        Toast.makeText(this@SignUpActivity1, "환영합니다. 로그인해주세요", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignUpActivity1, LoginActivity1::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d("Signup", response.code().toString())
                        Toast.makeText(this@SignUpActivity1, setMessage(response.code().toString()), Toast.LENGTH_SHORT).show()
                        Log.d("Signup", "회원가입X")
                    }
                }
                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Log.e("Signup", t.localizedMessage)
                }
            })
        }
    }

    fun setMessage(w: String): String {
        when(w) {
            "401" -> return "이메일 형식이 아닙니다."
            "402" -> return "비밀번호가 8자리보다 작습니다."
            "403" -> return "이미 존재하는 이메일입니다."
            else -> return "회원가입 할 수 없습니다."
        }
    }

}