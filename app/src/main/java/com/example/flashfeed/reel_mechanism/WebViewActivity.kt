package com.example.flashfeed.reel_mechanism

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.appcompat.app.AppCompatActivity
// Class to handle WebView functionality
// Implement WebView functionality here


class WebViewActivity: AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        val articleLink = intent.getStringExtra("url")
        webView.loadUrl(articleLink ?: "https://www.example.com")
        setContentView(webView)
    }
}