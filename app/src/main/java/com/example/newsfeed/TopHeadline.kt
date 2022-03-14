package com.example.newsfeed

import java.io.Serializable

data class TopHeadline(
    val title: String,
    val thumbnailUrl: String,
    val source: String,
    val iconUrl: String,
    val content: String
) : Serializable