package com.alexstephanov.brestnews.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alexstephanov.brestnews.*
import com.alexstephanov.brestnews.models.News
import com.alexstephanov.brestnews.models.NewsAPI
import com.alexstephanov.brestnews.models.NewsItem
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{

    private lateinit var list: RecyclerView
    private var request: Disposable? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        val mainTitle: TextView = toolbar.findViewById(R.id.main_title)
        val searchEditText: EditText = toolbar.findViewById(R.id.search_edit_text)
        val searchButtonShow: Button = toolbar.findViewById(R.id.search_button_show)
        val searchButton: Button = toolbar.findViewById(R.id.search_button)
        val backButton: ImageView = toolbar.findViewById(R.id.back_button)

        searchButtonShow.setOnClickListener {
            mainTitle.visibility = View.INVISIBLE
            searchButtonShow.visibility = View.INVISIBLE
            searchEditText.visibility = View.VISIBLE
            searchButton.visibility = View.VISIBLE
            backButton.visibility = View.VISIBLE
        }
        backButton.setOnClickListener {
            mainTitle.visibility = View.VISIBLE
            searchButtonShow.visibility = View.VISIBLE
            searchEditText.visibility = View.INVISIBLE
            searchButton.visibility = View.INVISIBLE
            backButton.visibility = View.INVISIBLE
            showList()
        }
        list = findViewById(R.id.recycler_view_main_list)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_main)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            onRefresh()
        }

        showList()
        startRequest()

        searchButton.setOnClickListener {
            val text = searchEditText.text.toString()

            Realm.getDefaultInstance().executeTransaction { realm ->
                val result = realm.where(NewsItem::class.java).contains("title", text).findAll()
                if(result.size > 0) {
                    val items: RealmList<NewsItem> = RealmList()
                    val s: ArrayList<String> = ArrayList()
                    for(i in 0 until result.size) {
                        if(!s.contains(result[i]!!.title)) {
                            items.add(
                                NewsItem(
                                    result[i]!!.title,
                                    result[i]!!.description,
                                    result[i]!!.thumbnail,
                                    result[i]!!.date,
                                    result[i]!!.link,
                                    result[i]!!.text
                                )
                            )
                            s.add(result[i]!!.title)
                        }
                    }
                    list.adapter = DataAdapter(items)
                    list.layoutManager = LinearLayoutManager(this)
                }
            }
        }
    }

    override fun onRefresh() {
        Handler().postDelayed({
            startRequest()
            swipeRefreshLayout.isRefreshing = false
        }, 3000)
    }

    private fun showList() {
        Realm.getDefaultInstance().executeTransaction { realm ->
            val news = realm.where(News::class.java).findAll()
            if(news.size > 0) {
                list.adapter = DataAdapter(news[0]!!.items)
                list.layoutManager = LinearLayoutManager(this)
            }
        }
    }

    fun showArticle(title: String, thumbnail: String, date: String, link: String, content: ArrayList<String>) {
        val intent = Intent(this, DetailedActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("thumbnail", thumbnail)
        intent.putExtra("date", date)
        intent.putExtra("link", link)
        intent.putExtra("content", content)
        startActivity(intent)
    }

    private fun startRequest() {
        val observable = createRequest("http://pawellon.beget.tech")
            .map { Gson().fromJson(it, NewsAPI::class.java)}
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = observable.subscribe({

            val news = News(it.items.mapTo(RealmList(), { news -> NewsItem(news.title, news.description, news.thumbnail, news.date, news.link, news.text) }))

            Realm.getDefaultInstance().executeTransaction { realm ->

                val oldList = realm.where(News::class.java).findAll()
                Log.d("tag", "completed")
                if(oldList.size > 0)
                    oldList.deleteAllFromRealm()
                realm.copyToRealm(news)
            }
            Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show()
            showList()
        }, {
            Log.e("tag", "", it)
            showList()
            Toast.makeText(this, "Проверьте подключение к интернету!", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onClick(v: View?) {
    }
}