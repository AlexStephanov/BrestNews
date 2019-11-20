package com.alexstephanov.brestnews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ItemClickListener {

    val itemTitle: String = "ITEM_TITLE"
    val itemDescription: String = "ITEM_DESCRIPTION"
    val defaultItemTitle: String = ""
    val defaultItemDescription: String = ""

    var request: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val observable = createRequest("http://pawellon.beget.tech/onlinebrest.php")
            .map {Gson().fromJson(it, News::class.java)}
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = observable.subscribe({
            for(item in it.items) {
                initList(it.items)
            }
        }, {
            Log.e("tag", "", it)
        })
    }

    private fun initList(items: ArrayList<NewsItem>) {
        val list : RecyclerView = findViewById(R.id.recycler_view_main_list)
        val listAdapter = DataAdapter(items)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = listAdapter

        //listAdapter.setItemClickListener(this)
        //listAdapter.update(items)
    }

    private fun initListItemModels(): ArrayList<NewsItem> {

        val items =  ArrayList<NewsItem>()
        //val item = NewsItem("Hi", "HELLO", "THERE", "ffdsf", "trt")

        repeat(10) {
          //  items.add(item)
        }

        return items
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<DataAdapter.ViewHolder>) {
        //val news = null
        val intent = Intent(this, NewsActivity::class.java)
        //intent.putExtra(itemTitle, news.title)
        //intent.putExtra(itemDescription, news.description)

        startActivity(intent)
    }

    override fun onDestroy() {
        request?.dispose()
        super.onDestroy()
    }
}