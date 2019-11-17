package com.alexstephanov.brestnews

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initList()
    }

    private fun initList() {
        val list : RecyclerView = findViewById(R.id.recycler_view_main_list)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = DataAdapter(initListItemModels())
    }

    @SuppressLint("ResourceType")
    fun initListItemModels(): MutableList<ListItem> {

        val items: MutableList<ListItem> =  mutableListOf()
        val item = ListItem(R.mipmap.ic_launcher, "Brest", "News")

        repeat(10) {
            items.add(item)
        }

        return items
    }
}
