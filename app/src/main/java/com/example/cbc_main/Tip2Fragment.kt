package com.example.cbc_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class Tip2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_tip2, container, false)
    }

    internal fun newInstant() : Tip2Fragment
    {
        val args = Bundle()
        val frag = Tip2Fragment()
        frag.arguments = args
        return frag
    }


}