package com.alexstephanov.brestnews.listeners

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alexstephanov.brestnews.ui.DataAdapter

interface ItemClickListener {
    fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<DataAdapter.ViewHolder>)
}