package com.example.newsfeed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity(){

    private lateinit var termSearchBox: SearchView
    private lateinit var searchButton: Button
    private lateinit var viewMapButton: Button
    private lateinit var viewTopHeadlneButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var headlineRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefs: SharedPreferences = getSharedPreferences("NewsFeed", Context.MODE_PRIVATE)

        termSearchBox = findViewById(R.id.termSearch)
        searchButton = findViewById(R.id.searchHome)
        viewMapButton = findViewById(R.id.viewMap)
        viewTopHeadlneButton = findViewById(R.id.viewTopHeadlines)
        progressBar = findViewById(R.id.progressBar)
        headlineRecycler = findViewById(R.id.headlineRecycler)


        progressBar.visibility = View.INVISIBLE
        searchButton.isEnabled = false

        val savedQuery = sharedPrefs.getString("SAVED_QUERY", "")

        searchButton.isEnabled = savedQuery!!.isNotBlank()

        // Save and load the previous query
        termSearchBox.setQuery(savedQuery, false)
        termSearchBox.setOnSearchClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    SourceScreen::class.java
                ).putExtra("quickSearchQuery", termSearchBox.query.toString())
            )
            sharedPrefs.edit().putString("SAVED_QUERY", termSearchBox.query.toString()).apply()
        }
        //Homepage recycler layout
        var news: List<TopHeadline> = listOf()

        val topHeadlineAdapter = TopHeadlineAdapter(news)
        doAsync {
            if (news == null) {
                Log.e("MapsActivity", "Empty Headline")
            }
            runOnUiThread {
                headlineRecycler.adapter = topHeadlineAdapter
                headlineRecycler.layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
        termSearchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //Save search
                startActivity(
                    Intent(
                        applicationContext,
                        SourceScreen::class.java
                    ).putExtra("quickSearchQuery", termSearchBox.query.toString())
                )
                sharedPrefs.edit().putString("SAVED_QUERY", query).apply()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchButton.isEnabled = !termSearchBox.query.isBlank()
                if (termSearchBox.query.isBlank()) {
                    sharedPrefs.edit().putString("SAVED_QUERY", "").apply()
                } else {
                    sharedPrefs.edit().putString("SAVED_QUERY", newText).apply()
                }
                return searchButton.isEnabled
            }
        })
        searchButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            startActivity(
                Intent(
                    applicationContext,
                    SourceScreen::class.java
                ).putExtra("quickSearchQuery", termSearchBox.query.toString())
            )
            sharedPrefs.edit().putString("SAVED_QUERY", termSearchBox.query.toString()).apply()
        }
        progressBar.visibility = View.INVISIBLE

        //Go to the map page
        viewMapButton.setOnClickListener { view: View ->
            Toast.makeText( getBaseContext(), "Long Press on the map",Toast.LENGTH_SHORT).show();
            val intent: Intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        // Go to to the top headline page
        viewTopHeadlneButton.setOnClickListener { view: View ->
            val intent: Intent = Intent(this, TopHeadlineScreen::class.java)
            startActivity(intent)
        }
    }
}