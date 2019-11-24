package com.alexstephanov.brestnews.ui

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alexstephanov.brestnews.R
import com.squareup.picasso.Picasso

class DetailedFragment : Fragment() {

    var title: String? = null
    var thumbnail: String? = null
    var date: String? = null
    var link: String? = null
    var text: ArrayList<String>? = null
    var img: ArrayList<String>? = null

    lateinit var titleTextView: TextView
    lateinit var thumbnailImageView: ImageView
    lateinit var dateTextView: TextView
    lateinit var showOriginalTextView: TextView
    lateinit var contentTextView: TextView
    lateinit var imagesImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = arguments?.getString("title")
        thumbnail = arguments?.getString("thumbnail")
        date = arguments?.getString("date")
        link = arguments?.getString("link")
        text = arguments?.getStringArrayList("text")
        img = arguments?.getStringArrayList("img")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detailed, container, false)

        titleTextView = view.findViewById(R.id.text_view_detailed_title)
        thumbnailImageView = view.findViewById(R.id.image_view_detailed_image)
        dateTextView = view.findViewById(R.id.text_view_detailed_date)
        showOriginalTextView = view.findViewById(R.id.text_view_detailed_show_original)
        contentTextView = view.findViewById(R.id.text_view_detailed_content)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        titleTextView.text = title
        Picasso.get().load(thumbnail).into(thumbnailImageView)
        dateTextView.text = date

        val showOriginalText = "Источник: ${getNameOfSource()}\nПоказать оригинал"
        showOriginalTextView.text = showOriginalText

        var content = ""
        for(s in text!!)
            content += "    $s\n\n"
        content = content.replace("&laquo;", "\"")
        content = content.replace("&raquo;", "\"")
        content = content.replace("&nbsp;", " ")
        content = content.replace("&ndash;", "-")
        content = content.replace("&mdash;", "-")
        content = content.replace("&hellip;", "...")
        content = content.replace("&bdquo;", "\"")
        content = content.replace("&ldquo;", "\"")
        content = content.replace("&minus;", "-")
        content = content.replace("\\", "")

        contentTextView.text = content
        showOriginalTextView.setOnClickListener {
            link?.let { it1 -> (thumbnailImageView.context as MainActivity).browseArticle(it1) }
        }
    }

    private fun getNameOfSource() : String = when {
        link!!.contains("onlinebrest") -> "onlinebrest.by"
        link!!.contains("virtualbrest") -> "virtualbrest.by"
        link!!.contains("brestcity") -> "brestcity.com"
        else -> "unknown"
    }
}