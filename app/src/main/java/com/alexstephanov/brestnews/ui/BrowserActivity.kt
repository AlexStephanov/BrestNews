package com.alexstephanov.brestnews.ui

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.alexstephanov.brestnews.R

class BrowserActivity : AppCompatActivity() {

    lateinit var webView: WebView
    var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        webView = findViewById(R.id.web_view_browser)

        val intent = intent
        link = intent.getStringExtra("link")

        webView.loadUrl(link)
    }
}