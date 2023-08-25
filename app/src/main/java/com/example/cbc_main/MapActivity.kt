package com.example.cbc_main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val webView: WebView = findViewById(R.id.webViewMap)
        webView.apply {

            webView.setInitialScale(getgetScale(3600.0))
            webChromeClient = WebChromeClient() //클릭시 새창 안뜨게
            settings.javaScriptEnabled = true // 새창띄우기 허용여부
            settings.setSupportMultipleWindows(true)//메타태크 허용여부
            settings.javaScriptCanOpenWindowsAutomatically = true//화면사이즈 맞추기 허용 여부
            settings.loadWithOverviewMode = true//화면 줌 허용여부
            settings.setSupportZoom(true)//화면 줌 허용여부
            settings.builtInZoomControls = true//화면 확대 축소 허용여부
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true

            settings.cacheMode = WebSettings.LOAD_NO_CACHE

            val url = "http://parkbomin.iptime.org:8081/html/mapsearch.html"
            webView.loadUrl(url)
        }
    }

    private fun getgetScale(size: Double): Int {
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = display.width
        var scale : Double = width / size
        scale = scale * 100.0
        return scale.toInt()
    }
}