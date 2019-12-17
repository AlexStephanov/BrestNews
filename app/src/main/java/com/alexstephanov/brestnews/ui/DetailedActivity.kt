package com.alexstephanov.brestnews.ui

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alexstephanov.brestnews.R
import com.squareup.picasso.Picasso

class DetailedActivity : AppCompatActivity(), View.OnClickListener {

    var title: String? = null
    var thumbnail: String? = null
    var date: String? = null
    var link: String? = null
    var content: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val titleTextView: TextView = findViewById(R.id.text_view_detailed_title)
        val thumbnailImageView: ImageView = findViewById(R.id.image_view_detailed_image)
        val dateTextView: TextView = findViewById(R.id.text_view_detailed_date)
        val showOriginalTextView: TextView = findViewById(R.id.text_view_detailed_show_original)
        val contentTextView: TextView = findViewById(R.id.text_view_detailed_content)

        val intent = intent
        title = intent.getStringExtra("title")
        thumbnail = intent.getStringExtra("thumbnail")
        date = intent.getStringExtra("date")
        link = intent.getStringExtra("link")
        content = intent.getStringArrayListExtra("content")

        var newStr = title
        newStr = replaceSpecialSymbols(newStr!!)
        titleTextView.text = newStr
        Picasso.get().load(thumbnail).into(thumbnailImageView)
        dateTextView.text = date

        val showOriginalText = "Источник: ${getNameOfSource()}\nПоказать оригинал"
        showOriginalTextView.text = showOriginalText
        showOriginalTextView.setOnClickListener {
            browseArticle(link)
        }

        var convertedContent = ""
        for(s in content!!) {
            if(!s.contains("jpg") && !s.contains("<center>")) {
                convertedContent += "    $s\n\n"
            } else {
                Log.d("img", s)
            }
        }
        convertedContent = replaceSpecialSymbols(convertedContent)

        contentTextView.text = convertedContent
    }

    private fun browseArticle(link: String?) {
        val intent = Intent(this, BrowserActivity::class.java)
        intent.putExtra("link", link)
        startActivity(intent)
    }

    private fun getNameOfSource() : String = when {
        link!!.contains("onlinebrest") -> "onlinebrest.by"
        link!!.contains("brestcity") -> "brestcity.com"
        link!!.contains("vb.by") -> "vb.by"
        else -> "unknown"
    }

    private fun replaceSpecialSymbols(content: String) : String {
        var convertedContent = ""
        if(content.contains("&") && content.contains(";")) {
            convertedContent = content.replace("&laquo;", "\"")
            convertedContent = convertedContent.replace("&raquo;", "\"")
            convertedContent = convertedContent.replace("&nbsp;", " ")
            convertedContent = convertedContent.replace("&ndash;", "-")
            convertedContent = convertedContent.replace("&mdash;", "-")
            convertedContent = convertedContent.replace("&hellip;", "...")
            convertedContent = convertedContent.replace("&bdquo;", "\"")
            convertedContent = convertedContent.replace("&ldquo;", "\"")
            convertedContent = convertedContent.replace("&minus;", "-")
            convertedContent = convertedContent.replace("&rsquo;", "\"")
            convertedContent = convertedContent.replace("&rdquo;", "\"")
            convertedContent = convertedContent.replace("\\", "")
            convertedContent = convertedContent.replace("\\\\", "")

            return convertedContent
        }
        return content
    }

    override fun onClick(v: View?) {
    }
}