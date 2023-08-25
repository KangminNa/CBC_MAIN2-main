package com.example.cbc_main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.cbc_main.databinding.FragmentBanner3Binding


class Banner3Fragment : Fragment() {



    private var _binding: FragmentBanner3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBanner3Binding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnMoveLogin.setOnClickListener {
            val intent = Intent(getActivity(), LoginActivity1::class.java)
            startActivity(intent)
        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


