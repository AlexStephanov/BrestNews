package com.alexstephanov.brestnews.models

import io.realm.RealmList
import io.realm.RealmObject

open class News (
    var items: RealmList<NewsItem> = RealmList()
) : RealmObject()