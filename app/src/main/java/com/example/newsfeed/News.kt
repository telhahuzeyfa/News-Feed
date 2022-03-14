package com.example.newsfeed

import java.io.Serializable

data class News(
    val title: String,
    val thumbnailUrl: String,
    val source: String,
    val url: String,
    val content: String
    ): Serializable
