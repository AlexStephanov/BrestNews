package com.alexstephanov.brestnews

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface ItemClickListener {
    fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<DataAdapter.ViewHolder>)
}