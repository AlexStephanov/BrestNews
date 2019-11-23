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

    lateinit var titleTextView: TextView
    lateinit var thumbnailImageView: ImageView
    lateinit var dateTextView: TextView
    lateinit var showOriginalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = arguments?.getString("title")
        thumbnail = arguments?.getString("thumbnail")
        date = arguments?.getString("date")
        link = arguments?.getString("link")
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

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        titleTextView.text = title
        Picasso.get().load(thumbnail).into(thumbnailImageView)
        dateTextView.text = date

        val showOriginalText = "Источник: ${getNameOfSource()}\nПоказать оригинал"

        showOriginalTextView.text = showOriginalText

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