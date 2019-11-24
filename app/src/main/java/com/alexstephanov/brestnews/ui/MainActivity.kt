package com.alexstephanov.brestnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alexstephanov.brestnews.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_place, MainFragment())
                .commitAllowingStateLoss()
        }
    }

    fun browseArticle(link: String) {
        val bundle = Bundle()
        bundle.putString("link", link)
        val fragment = BrowserFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragment_place, fragment).addToBackStack("main").commitAllowingStateLoss()
    }

    fun showArticle(title: String, thumbnail: String, date: String, link: String, text: ArrayList<String>, img: ArrayList<String>) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("thumbnail", thumbnail)
        bundle.putString("date", date)
        bundle.putString("link", link)
        bundle.putStringArrayList("text", text)
        bundle.putStringArrayList("img", img)
        val fragment = DetailedFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragment_place, fragment).addToBackStack("main").commitAllowingStateLoss()
    }
}