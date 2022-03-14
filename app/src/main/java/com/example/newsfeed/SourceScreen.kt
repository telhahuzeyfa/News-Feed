package com.example.newsfeed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class SourceScreen : AppCompatActivity() {
    private lateinit var spinnerCategory: Spinner
    private lateinit var selectSourcesText: TextView
    private lateinit var sourcesRecyclerView: RecyclerView
    private lateinit var sourceAdapter: SourceAdapter
    private lateinit var skipSourceButton: Button

    //initially selected category
    private  var selectedCategoryIndex = 0
    var selectedSources = arrayListOf<String>()
    companion object{
        var listOfCategories = arrayOf("Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sources_screen)

        var query = intent.getStringExtra("quickSearchQuery")

        val newsAPIKey = getString(R.string.news_api_key)


        spinnerCategory = findViewById(R.id.spinnerCatagory)
        selectSourcesText = findViewById(R.id.selectSourcesText)
        sourcesRecyclerView = findViewById(R.id.sourcesRecycler)
        skipSourceButton = findViewById(R.id.skipSourceButton)

        this.title = "Search for $query"

        //Initialize the spinner adapter
        val categorySpinnerAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                listOfCategories
            )
        //Initialize the category spinner
        spinnerCategory.adapter = categorySpinnerAdapter
        spinnerCategory.setSelection(selectedCategoryIndex)

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectSourcesText.text = "0 Sources Selected"
                selectSourcesText.text = "Sources (select at least 1)"
                selectedCategoryIndex = position
                val newsManger = NewsManager()
                doAsync {
                    val sources: List<Source> =
                    newsManger.retrieveSources(newsAPIKey, listOfCategories[selectedCategoryIndex])
                    sourceAdapter = SourceAdapter(sources)
                    runOnUiThread {
                        selectedSources = sourceAdapter.getCheckList()
                        sourcesRecyclerView.adapter = sourceAdapter
                        sourcesRecyclerView.layoutManager = LinearLayoutManager(this@SourceScreen)
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
       skipSourceButton.setOnClickListener { view: View ->
           var query = intent.getStringExtra("quickSearchQuery")
           this.title = "Search results for $query"

            val newsManger = NewsManager()
            doAsync {
                for (element in listOfCategories){
                    val sources: List<Source> =
                    newsManger.retrieveSources(newsAPIKey, element)
                    sourceAdapter = SourceAdapter(sources)
                }
                runOnUiThread {
                    selectedSources = sourceAdapter.getCheckList()
                    sourcesRecyclerView.adapter = sourceAdapter
                    sourcesRecyclerView.layoutManager = LinearLayoutManager(this@SourceScreen)
                }
            }
        }
    }
}