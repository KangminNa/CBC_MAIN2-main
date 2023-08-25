package com.example.cbc_main

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cbc_main.databinding.FragmentBanner1Binding
import com.example.cbc_main.databinding.FragmentBanner3Binding

class Banner1Fragment : Fragment() {
    private var _binding: FragmentBanner1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBanner1Binding.inflate(inflater, container, false)
        val view = binding.root


//        ObjectAnimator.ofFloat(binding.imgBanner1Text, "translationY", -50f).apply {
//            duration = 1000
//            repeatCount = -1
//            repeatMode = ValueAnimator.REVERSE
//            start()
//        }

        ObjectAnimator.ofFloat(binding.imgBanner1Wheel, "translationY", 100f).apply {
            duration = 1500
            repeatCount = -1
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}