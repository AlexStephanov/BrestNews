package com.alexstephanov.brestnews.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alexstephanov.brestnews.R
import com.alexstephanov.brestnews.createRequest
import com.alexstephanov.brestnews.models.News
import com.alexstephanov.brestnews.models.NewsAPI
import com.alexstephanov.brestnews.models.NewsItem
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList

class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var list: RecyclerView
    private var request: Disposable? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        list = view!!.findViewById(R.id.recycler_view_main_list)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_main)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            onRefresh()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val observable = createRequest("http://pawellon.beget.tech/onlinebrest.php")
            .map { Gson().fromJson(it, NewsAPI::class.java)}
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        request = observable.subscribe({

            val news = News(it.items.mapTo(RealmList(), { news -> NewsItem(news.title, news.description, news.thumbnail, news.date, news.link, news.text, news.img) }))

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
            Toast.makeText(activity, "Проверьте подключение к интернету!", Toast.LENGTH_SHORT).show()
        })
    }

    private fun showList() {
        Realm.getDefaultInstance().executeTransaction { realm ->
            //if(!isVisible)
                //return@executeTransaction
            val news = realm.where(News::class.java).findAll()
            if(news.size > 0) {
                list.adapter = DataAdapter(news[0]!!.items)
                list.layoutManager = LinearLayoutManager(activity)
            }
        }
    }

    override fun onRefresh() {
        Handler().postDelayed({
            val observable = createRequest("http://pawellon.beget.tech/onlinebrest.php")
                .map { Gson().fromJson(it, NewsAPI::class.java)}
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            request = observable.subscribe({

                val news = News(it.items.mapTo(RealmList(), { news -> NewsItem(news.title, news.description, news.thumbnail, news.date, news.link, news.text, news.img) }))

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
                Toast.makeText(activity, "Проверьте подключение к интернету!", Toast.LENGTH_SHORT).show()
            })
            swipeRefreshLayout.isRefreshing = false
        }, 10000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        request?.dispose()
    }
}