package com.example.cbc_main

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.cbc_main.databinding.FragmentBanner2Binding
import com.example.cbc_main.databinding.FragmentBanner3Binding


class Banner2Fragment : Fragment() {
    private var _binding: FragmentBanner2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBanner2Binding.inflate(inflater, container, false)
        val view = binding.root




        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}