package com.alexstephanov.brestnews.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
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

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var list: RecyclerView
    private var request: Disposable? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        toolbar.setTitle(R.string.main_title)
        setSupportActionBar(toolbar)

        list = findViewById(R.id.recycler_view_main_list)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_main)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            onRefresh()
        }

        val observable = createRequest("http://pawellon.beget.tech")
            .map { Gson().fromJson(it, NewsAPI::class.java)}
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = observable.subscribe({

            val news = News(it.items.mapTo(RealmList(), { news -> NewsItem(news.title, news.description, news.thumbnail, news.date, news.link, news.text) }))

            Realm.getDefaultInstance().executeTransaction { realm ->

                val oldList = realm.where(News::class.java).findAll()
                if(oldList.size > 0)
                    for(item in oldList)
                        item.deleteFromRealm()
                realm.copyToRealm(news)
            }
            showList()
        }, {
            Log.e("tag", "", it)
            showList()
            Toast.makeText(this, "Проверьте подключение к интернету!", Toast.LENGTH_SHORT).show()
        })
        }

    override fun onRefresh() {
        Handler().postDelayed({
            val observable = createRequest("http://pawellon.beget.tech")
                .map { Gson().fromJson(it, NewsAPI::class.java)}
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            request = observable.subscribe({

                val news = News(it.items.mapTo(RealmList(), { news -> NewsItem(news.title, news.description, news.thumbnail, news.date, news.link, news.text) }))

                Realm.getDefaultInstance().executeTransaction { realm ->

                    val oldList = realm.where(News::class.java).findAll()
                    if(oldList.size > 0)
                        for(item in oldList)
                            item.deleteFromRealm()
                    realm.copyToRealm(news)
                }
                showList()
            }, {
                Log.e("tag", "", it)
                showList()
                Toast.makeText(this, "Проверьте подключение к интернету!", Toast.LENGTH_SHORT).show()
            })
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
        /*val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("thumbnail", thumbnail)
        bundle.putString("date", date)
        bundle.putString("link", link)
        bundle.putStringArrayList("text", text)
        val fragment = DetailedFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.fragment_place, fragment).addToBackStack("main").commitAllowingStateLoss()*/
        val intent = Intent(this, DetailedActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("thumbnail", thumbnail)
        intent.putExtra("date", date)
        intent.putExtra("link", link)
        intent.putExtra("content", content)
        startActivity(intent)
    }
}