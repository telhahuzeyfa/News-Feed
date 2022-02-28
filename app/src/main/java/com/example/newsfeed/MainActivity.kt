package com.example.newsfeed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity(){

    private lateinit var termSearchBox: EditText
    private lateinit var searchButton: Button
    private lateinit var viewMapButton: Button
    private lateinit var viewTopHeadlneButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefs: SharedPreferences = getSharedPreferences("NewsFeed", Context.MODE_PRIVATE)

        termSearchBox = findViewById(R.id.termSearch)
        searchButton = findViewById(R.id.searchHome)
        viewMapButton = findViewById(R.id.viewMap)
        viewTopHeadlneButton = findViewById(R.id.viewTopHeadlines)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.INVISIBLE
        searchButton.isEnabled = false

        termSearchBox.addTextChangedListener(textWatcher)
        searchButton.setOnClickListener { view: View ->
            val inputtedText: String = termSearchBox.text.toString()

            // Save the username to SharedPreferences
            sharedPrefs
                .edit()
                .putString("TEXT", inputtedText)
                .apply()

            progressBar.visibility = View.VISIBLE

            // An Intent is used to start a new Activity.
           val intent: Intent = Intent(this, SourceScreen::class.java)

            //Start Activity
            startActivity(intent)
        }
        viewMapButton.setOnClickListener { view: View ->
            val intent: Intent = Intent(this, MapsActivity::class.java)

            startActivity(intent)
        }
        // Using the same TextWatcher instance for both EditTexts so the same block of code runs on each character.
        termSearchBox.addTextChangedListener(textWatcher)

        //Restore the saved value from SharedPreference and display it to the user when the screen loads
        val savedSearchValues = sharedPrefs.getString("TEXT", "")
    }
    private val textWatcher: TextWatcher = object: TextWatcher{
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val inputtedText: String = termSearchBox.text.toString()
            val enableButton: Boolean = inputtedText.isNotBlank()
            searchButton.isEnabled = enableButton
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }
}