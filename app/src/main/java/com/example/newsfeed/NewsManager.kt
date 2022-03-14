package com.example.newsfeed

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

class NewsManager {

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        okHttpClient = builder.build()
    }
    fun retrieveSources(newsApiKey: String, catagoryQuery: String): List<Source> {

        val listOfSources = mutableListOf<Source>()

        //Request api Key
        val request: Request = Request.Builder()

            .url("https://newsapi.org/v2/top-headlines/sources?apiKey=c2fcdaba168f4ced9261152d382caa92&category=$catagoryQuery")
            .header("Authorization", "$newsApiKey")
            .build()
        //calling the request
        val response: Response = okHttpClient.newCall(request).execute()

       //Get the json body
        val responseBody: String? = response.body?.string()

        // The .isSuccessful checks to see if the status code is 200-299
        if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseBody)
            val sources = json.getJSONArray("sources")
            //parse as News Object to headlines
            for (i in 0 until sources.length()) {
                val curr: JSONObject = sources.getJSONObject(i)
                val title: String = curr.getString("name")
                val content = curr.getString("description")
                val url = curr.getString("url")
                listOfSources.add(Source(title, content, url))
            }
        } else {
            return emptyList()
        }
        return listOfSources
    }
    //Get top headlines
    fun fetchHeadlinesNews(newsAPI_Key: String, queryCategory: String): List<TopHeadline> {
        val newsList = mutableListOf<TopHeadline>()
        //build the request
        var request: Request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines?country=us&category=${queryCategory}&apiKey=c2fcdaba168f4ced9261152d382caa92")
            .header("Authorization", "$newsAPI_Key")
            .build()

        //Calling the request
        val response = okHttpClient.newCall(request).execute()

        //Get the json body
        val responseString = response.body?.string()

        //check response is not empty or null
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseString)

            val articles = json.getJSONArray("articles")
            //parse as News Object to headlines
            for (i in 0 until articles.length()) {
                var currNews = articles.getJSONObject(i)
                val title = currNews.getString("title")
                val thumbnailUrl = currNews.getString("urlToImage")
                val source = currNews.getJSONObject("source").getString("name")
                val url = currNews.getString("url")
                val content = currNews.getString("content")
                newsList.add(TopHeadline(title, thumbnailUrl, source, url, content))
            }
        }
        return newsList
    }
    fun fetchMapNews(newsApiKey: String, address: List<android.location.Address>): List<News> {
        val newsList = mutableListOf<News>()
        //build the request
        var request: Request = when {
            address.first().adminArea.isNullOrEmpty() -> {
                return emptyList()
            }
            address.first().countryCode == "US" -> {
                Request.Builder()
                    .url("https://newsapi.org/v2/everything?qInTitle=${address.first().adminArea}")
                    .header("Authorization", "$newsApiKey")
                    .build()
            }
            else -> {
                Request.Builder()
                    .url("https://newsapi.org/v2/everything?qInTitle=${address.first().countryName}")
                    .header("Authorization", "$newsApiKey")
                    .build()
            }
        }
        //call the request
        val response = okHttpClient.newCall(request).execute()
        //retrieve the json body
        val responseString = response.body?.string()
        //check response is not empty or null
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            //parse json string
            val json = JSONObject(responseString)
            val articles = json.getJSONArray("articles")
            //parse as News Object to headlines
            for (i in 0 until articles.length()) {
                var currNews = articles.getJSONObject(i)
                val title = currNews.getString("title")
                val thumbnailUrl = currNews.getString("urlToImage")
                val source = currNews.getJSONObject("source").getString("name")
                val url = currNews.getString("url")
                val content = currNews.getString("content")
                newsList.add(News(title, thumbnailUrl, source, url, content))
            }
        }
        return newsList
    }
}
