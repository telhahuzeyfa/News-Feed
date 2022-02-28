package com.example.newsfeed

import java.io.Serializable

data class Source(val titleSource: String, val content: String, var isChecked: Boolean) :
    Serializable
