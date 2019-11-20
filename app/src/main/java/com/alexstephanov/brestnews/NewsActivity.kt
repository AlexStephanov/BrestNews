package com.alexstephanov.brestnews

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val extras = intent.extras
        val itemTitle = extras?.getString(MainActivity::itemTitle.toString(), MainActivity::defaultItemTitle.toString())
        val itemDescription = extras?.getString(MainActivity::itemDescription.toString(), MainActivity::defaultItemDescription.toString())

        val titleTextView: TextView = findViewById(R.id.text_view_news_title)
        val descriptionTextView: TextView = findViewById(R.id.text_view_news_description)

        titleTextView.text = itemTitle
        descriptionTextView.text = itemDescription

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}