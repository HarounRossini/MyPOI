package com.example.mypoi

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mypoi.databinding.ActivityMapsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mypoi.category.CategoryActivity
import com.mypoi.location.AddLocationActivity
import database.Database
import utils.Utils

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentLocation: Location? = null
    private val dbHelper = Database(this)

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentLocation = Utils.getLocation(this, this)
        currentLocation = Utils.currentLocation


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // view category
        findViewById<FloatingActionButton>(R.id.categories).setOnClickListener{
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
        // view location
        findViewById<FloatingActionButton>(R.id.addLocation).setOnClickListener{
            val intent = Intent(this, AddLocationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        currentLocation = Utils.getLocation(this, this)
        currentLocation = Utils.currentLocation
        // Add a marker in Sydney and move the camera
        var home = currentLocation?.let { LatLng(currentLocation!!.latitude, it.longitude ) }
        if(home == null)
            home = LatLng(45.0, 8.0)
        // home?.let { MarkerOptions().position(it).title("Casa") }?.let { mMap.addMarker(it) }
        home?.let { CameraUpdateFactory.newLatLng(it) }?.let { mMap.moveCamera(it) }

        val locations = dbHelper.getLocations(dbHelper.writableDatabase)


        for(location in locations) {
            val position = LatLng(location.x.toDouble(), location.y.toDouble())
            Log.d("location", position.toString())
            mMap.addMarker(MarkerOptions().position(position).title(location.title))
        }
    }
}