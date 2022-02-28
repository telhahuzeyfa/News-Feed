package com.example.newsfeed

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

class NewsManager {

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()

        // This will cause all network traffic to be logged to the console for easy debugging
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        okHttpClient = builder.build()
    }
    fun retrieveSources(newsApiKey: String, catagoryQuery: String): List<Source> {

        val listOfSources = mutableListOf<Source>()
        //Request api Key
        val request: Request = Request.Builder()

            .url("https://newsapi.org/v2/top-headlines/sources?apiKey=727a0f87156242608f41b76285336687&category=$catagoryQuery")
            .get()
            .build()

        // This executes the request and waits for a response from the server
        val response: Response = okHttpClient.newCall(request).execute()
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

                listOfSources.add(Source(title, content, false))
            }
        } else {
            return emptyList()
        }
        return listOfSources
    }
}
