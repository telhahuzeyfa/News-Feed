package com.example.newsfeed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.doAsync

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var localNews: CardView
    private lateinit var localNewsRecycler: RecyclerView
    private lateinit var localMapResultsScreen: TextView

    private val logD = "MapsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        localNews = findViewById(R.id.mapsLocalNewsCard)
        localNewsRecycler = findViewById(R.id.localNewsRecycler)
        localNews.visibility = View.GONE
        localMapResultsScreen = findViewById(R.id.maps_titleMapResultsScreen)
        this.title = "News by location"


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        // Load saved coordinates
        val sharedPrefs: SharedPreferences =
            getSharedPreferences("android-news", Context.MODE_PRIVATE)
        var latitude = sharedPrefs.getString("SAVED_LATITUDE", "0.0")!!.toDouble()
        var longitude = sharedPrefs.getString("SAVED_LONGITUDE", "0.0")!!.toDouble()
        var loadCoordinates = LatLng(latitude, longitude)

        mMap = googleMap
        val newsAPIKey = getString(R.string.news_api_key)
        var news: List<News> = listOf()

        doAsync {
            val geocode = Geocoder(this@MapsActivity)
            //Get the default address
            val results: List<Address> = try {
                geocode.getFromLocation(
                    loadCoordinates.latitude,
                    loadCoordinates.longitude,
                    10
                )
            } catch (exception: Exception) {
                listOf()
            }

            // Cannot find address
            if (results == null || results == emptyList<Address>()) {
                localMapResultsScreen.text = "Cannot Load Current Address"
            } else {
                localMapResultsScreen.text = "Results for ${results.first().adminArea}"
                val temp = NewsManager()
                news = temp.fetchMapNews(newsAPIKey, results)
            }
            val newsAdapter = NewsAdapter(news)
            runOnUiThread {
                sharedPrefs.edit().putString("SAVED_LATITUDE", loadCoordinates.latitude.toString())
                    .apply()
                sharedPrefs.edit()
                    .putString("SAVED_LONGITUDE", loadCoordinates.longitude.toString())
                    .apply()

                if (results.size != 0) {
                    mMap.addMarker(
                        MarkerOptions().position(loadCoordinates).title(results.first().adminArea)
                    )
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLng(loadCoordinates))
                localNews.visibility = View.VISIBLE
                localNewsRecycler.adapter = newsAdapter
                localNewsRecycler.layoutManager = LinearLayoutManager(
                    this@MapsActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
        mMap.setOnMapLongClickListener { coord: LatLng ->
            mMap.clear()
            doAsync {
                val geocode = Geocoder(this@MapsActivity)
                sharedPrefs.edit().putString("SAVED_LATITUDE", coord.latitude.toString()).apply()
                sharedPrefs.edit().putString("SAVED_LONGITUDE", coord.longitude.toString()).apply()
                //Get the address of the pressed location
                val results: List<Address> = try {
                    geocode.getFromLocation(
                        coord.latitude,
                        coord.longitude,
                       5
                    )
                } catch (exception: Exception) {
                    Log.e("MapsActivity", "Geocoder failed:", exception)
                    listOf()
                }
                if (results == null || results == emptyList<Address>()) {
                    localMapResultsScreen.text = "Cannot Load Current Address"
                } else {
                    val temp = NewsManager()
                    news = temp.fetchMapNews(newsAPIKey, results)
                }
                val newsAdapter = NewsAdapter(news)
                when {
                    results.first().countryName == "United States" && results.first().adminArea != null && (news != null || news != emptyList<News>()) -> {
                        localMapResultsScreen.text = "Results for ${results.first().adminArea}"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                    results.first().countryName != null && (news != null || news != emptyList<News>()) -> {
                        localMapResultsScreen.text =
                            "Results for ${results.first().countryName}"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                    results.first().countryName == null -> {
                        localMapResultsScreen.text =
                            "No results for Ocean"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            Log.e("MapsActivity", "Results response received generating news")
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                    else -> {
                        localMapResultsScreen.text =
                            "No results for Ocean"
                        runOnUiThread {
                            mMap.addMarker(
                                MarkerOptions().position(coord).title(results[0].adminArea)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(coord))
                            localNews.visibility = View.VISIBLE
                            localNewsRecycler.adapter = newsAdapter
                            localNewsRecycler.layoutManager = LinearLayoutManager(
                                this@MapsActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }
            }
        }
    }
}