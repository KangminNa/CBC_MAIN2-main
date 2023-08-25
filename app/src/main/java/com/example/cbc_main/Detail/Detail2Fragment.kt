package com.example.cbc_main.Detail

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cbc_main.databinding.FragmentDetail2Binding

class Detail2Fragment : Fragment() {

    lateinit var detailActivity: DetailActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        detailActivity = context as DetailActivity
    }

    private var _binding: FragmentDetail2Binding?=null
    private val binding get()=_binding!!

    private var url :String = ""
    private var selected_date :String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentDetail2Binding.inflate(inflater, container, false)

        selected_date = UrlDate.urldate

        if(selected_date != UrlDate.urldate){
            selected_date = UrlDate.urldate
        }

        url = "http://parkbomin.iptime.org:18000/blackbox/"+selected_date+"/"

        binding.webViewVideo.settings.useWideViewPort = true
        binding.webViewVideo.settings.loadWithOverviewMode = true
        binding.webViewVideo.loadUrl(url)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        selected_date = UrlDate.urldate

        if(selected_date != UrlDate.urldate){
            selected_date = UrlDate.urldate
        }

        url = "http://parkbomin.iptime.org:18000/blackbox/"+selected_date+"/"

        binding.webViewVideo.settings.useWideViewPort = true
        binding.webViewVideo.settings.loadWithOverviewMode = true
        binding.webViewVideo.loadUrl(url)
    }

    private fun getgetScale(size: Double): Int {
        val display = (detailActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = display.width
        var scale : Double = width / size
        scale = scale * 100.0
        return scale.toInt()
    }
}