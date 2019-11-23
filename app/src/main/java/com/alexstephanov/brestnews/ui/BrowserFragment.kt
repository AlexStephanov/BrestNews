package com.alexstephanov.brestnews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.alexstephanov.brestnews.R

class BrowserFragment : Fragment() {

    var link: String? = null
    lateinit var webViewBrowser: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        link = arguments?.getString("link")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_browser, container, false)

        webViewBrowser = view.findViewById(R.id.web_view_browser)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        webViewBrowser.loadUrl(link)
    }
}