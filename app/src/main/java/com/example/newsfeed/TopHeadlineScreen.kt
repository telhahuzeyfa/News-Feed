package com.example.newsfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class TopHeadlineScreen : AppCompatActivity() {
    private lateinit var topHeadlineSpinner: Spinner
    private lateinit var selectTopHeadlineText: TextView
    private lateinit var topHeadlineRecycler: RecyclerView
    private lateinit var topHeadlineAdapter: TopHeadlineAdapter

    //initially selected category
    private  var selectedCategoryIndex = 0
    var selectedSources = arrayListOf<String>()
    companion object{
        var listOfCategories = arrayOf("Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top_headlines)

        val newsAPIKey = getString(R.string.news_api_key)


        topHeadlineSpinner = findViewById(R.id.topHeadlineSpinner)
        selectTopHeadlineText = findViewById(R.id.selectTopHeadlineText)
        topHeadlineRecycler = findViewById(R.id.topHeadlineRecycler)
        this.title = "Top Headline"

        //Initialize the spinner adapter
        val categorySpinnerAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOfCategories
            )
        //Initialize the category spinner
        topHeadlineSpinner.adapter = categorySpinnerAdapter
        topHeadlineSpinner.setSelection(selectedCategoryIndex)

        topHeadlineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectTopHeadlineText.text = "0 Sources Selected"
                selectTopHeadlineText.text = "Sources (select at least 1)"
                selectedCategoryIndex = position
                val newsManger = NewsManager()
                doAsync {
                    val topHeadline: List<TopHeadline> =
                        newsManger.fetchHeadlinesNews(newsAPIKey, listOfCategories[selectedCategoryIndex])
                    topHeadlineAdapter = TopHeadlineAdapter(topHeadline)
                    runOnUiThread {
                        selectedSources = topHeadlineAdapter.getCheckList()
                        topHeadlineRecycler.adapter = topHeadlineAdapter
                        topHeadlineRecycler.layoutManager = LinearLayoutManager(this@TopHeadlineScreen)
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }
}