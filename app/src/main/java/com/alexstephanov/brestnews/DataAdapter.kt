package com.alexstephanov.brestnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(private val items : MutableList<ListItem>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageImageView.setImageResource(items[position].image)
        holder.titleTextView.text = items[position].title
        holder.descriptionTextView.text = items[position].description
    }

    fun update(list: MutableList<ListItem>) {
        this.items.clear()
        this.items.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, val imageImageView: ImageView = itemView.findViewById(R.id.image_view_list_item), val titleTextView: TextView = itemView.findViewById(R.id.text_view_list_item_title), val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_list_item_description)) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        override fun onClick(v: View?) {

        }
    }
}