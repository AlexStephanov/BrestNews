package com.alexstephanov.brestnews

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DataAdapter(private val items: ArrayList<NewsItem>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    private var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView : View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun getItemByPosition(position: Int) : NewsItem {
        return items[position]
    }

    fun update(list: ArrayList<NewsItem>) {
        this.items.clear()
        this.items.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(item: NewsItem) {
            val thumbnailImageView: ImageView = itemView.findViewById(R.id.image_view_list_item_thumbnail)
            val titleTextView: TextView = itemView.findViewById(R.id.text_view_list_item_title)
            val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_list_item_description)

            titleTextView.text = item.title
            descriptionTextView.text = item.description

            Picasso.get().load(item.thumbnail).into(thumbnailImageView)
        }

        override fun onClick(v: View?) {
            //itemClickListener?.onItemClick(v!!, adapterPosition, this@DataAdapter)
        }

    }
}