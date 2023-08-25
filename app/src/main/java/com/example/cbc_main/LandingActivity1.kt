package com.example.cbc_main

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView

class LandingActivity1 : AppCompatActivity() {

//    private val landingLottieAnimation: LottieAnimationView by lazy { findViewById(R.id.landing_lottie) }
    private val imgJasla: ImageView by lazy{findViewById(R.id.imgJasla)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing1)

        imgAnimation()

    }

    private fun imgAnimation(){
        val imgFadeIn = ObjectAnimator.ofFloat(imgJasla, "alpha", 0f, 1f).apply { duration = 3000 }
        AnimatorSet().apply {
            play(imgFadeIn)
            start()
        }
        Handler().postDelayed({
            val intent = Intent(this@LandingActivity1, LandingActivity2::class.java)
            startActivity(intent)
            finish()
        }, 4000)

    }





//    private fun lottieAnimation(){
//        landingLottieAnimation.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animator: Animator) {
//
//            }
//            override fun onAnimationEnd(animator: Animator) {
//                landingLottieAnimation.setVisibility(View.GONE)
//
//                val intent = Intent(this@LandingActivity1, LandingActivity2::class.java)
//                startActivity(intent)
//                finish()
//
//            }
//
//            override fun onAnimationCancel(animator: Animator) {
//
//            }
//            override fun onAnimationRepeat(animator: Animator) {
//
//            }
//        })
//    }

}