package com.alexstephanov.brestnews.models

import io.realm.RealmList

class NewsItemAPI(
    val title: String,
    val description: String,
    val thumbnail: String,
    val date: String,
    val link: String,
    val text: RealmList<String>,
    val img: RealmList<String>
)