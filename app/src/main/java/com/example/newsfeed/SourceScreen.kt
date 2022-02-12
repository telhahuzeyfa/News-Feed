package com.example.newsfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.ArrayAdapter


class SourceScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sources_screen)

//        val spinner: Spinner = findViewById(R.id.spinner)
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.planets_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
    }

}