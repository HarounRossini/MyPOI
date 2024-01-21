package com.mypoi.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.mypoi.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import database.Database

class AddLocationActivity : AppCompatActivity() {
    var dbHelper = Database(this)

    private var currentLocation: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        // get db
        val writeDb = dbHelper.writableDatabase
        val readDib = dbHelper.readableDatabase

        // get location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionGranted()
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
            location: Location ->
                currentLocation = location
        }



        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_location)
        // get categories from db
        val categories = dbHelper.getCategories(readDib)

        // define spinner and its adapter to show categories
        val spinner = findViewById<Spinner>(R.id.categorySpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.toList())
        spinner.adapter = adapter

        //define x and y labels
        findViewById<TextView>(R.id.xLabel).text = currentLocation?.longitude?.toInt().toString()
        findViewById<TextView>(R.id.xLabel).text = currentLocation?.latitude?.toInt().toString()
    }

    // method to check Location permissions
    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            false
        } else {
            true
        }
    }



}