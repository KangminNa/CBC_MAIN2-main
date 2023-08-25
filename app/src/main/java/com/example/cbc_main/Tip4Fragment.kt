package com.example.cbc_main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.cbc_main.databinding.FragmentBanner1Binding
import com.example.cbc_main.databinding.FragmentTip4Binding


class Tip4Fragment : Fragment() {

    private var _binding: FragmentTip4Binding? = null
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTip4Binding.inflate(inflater, container, false)
        val view = binding.root

        val Ddb = Room.databaseBuilder(
            requireContext(), DistanceDatabase::class.java,
            "distance-db"
        ).allowMainThreadQueries().build()


        binding.tvCurKmFrame.text = "정비까지 남은" + 10000.minus(Ddb.DistanceDao().getDistance()) +"km"
        binding.tvCurKmChair.text = "정비까지 남은" + 1000.minus(Ddb.DistanceDao().getDistance()) +"km"
        binding.tvCurKmChain.text = "정비까지 남은" + 4000.minus(Ddb.DistanceDao().getDistance()) +"km"
        binding.tvCurKmWheel.text = "정비까지 남은" + 20000.minus(Ddb.DistanceDao().getDistance()) +"km"

        binding.pbTip4Frame.progress = Ddb.DistanceDao().getDistance()
        binding.pbTip4Chain.progress = Ddb.DistanceDao().getDistance()
        binding.pbTip4Chair.progress = Ddb.DistanceDao().getDistance()
        binding.pbTip4Wheel.progress = Ddb.DistanceDao().getDistance()

        binding.tvDelete.setOnClickListener {
            Ddb.DistanceDao().deleteDistance()
        }


        return view
    }

    internal fun newInstant() : Tip4Fragment
    {
        val args = Bundle()
        val frag = Tip4Fragment()
        frag.arguments = args
        return frag
    }


}