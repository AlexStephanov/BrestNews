package com.alexstephanov.brestnews.models

import io.realm.RealmList
import io.realm.RealmObject

open class NewsItem(
    var title: String = "",
    var description: String = "",
    var thumbnail: String = "",
    var date: String = "",
    var link: String = "",
    var text: RealmList<String> = RealmList(),
    var img: RealmList<String> = RealmList()
) : RealmObject()