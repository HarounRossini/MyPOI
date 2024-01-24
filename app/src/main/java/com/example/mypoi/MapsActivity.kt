package com.example.mypoi

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mypoi.databinding.ActivityMapsBinding
import com.example.mypoi.databinding.DialogBinding
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mypoi.category.CategoryActivity
import com.mypoi.location.AddLocationActivity
import database.Category
import database.Database
import utils.Utils

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentLocation: Location? = null
    private val dbHelper = Database(this)
    private var builder: AlertDialog.Builder? = null
    private var categories = ArrayList<Category>()
    private var categoryId: Int = 0

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        AlertDialog.Builder(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentLocation = Utils.getLocation(this, this)
        currentLocation = Utils.currentLocation

        // set up categories list
        categories = dbHelper.getCategories(dbHelper.readableDatabase)

        //if at least one category take first category Id as default categoryId
        if(categories.size > 0)
            categoryId = categories[0].id

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

            val newMarker = mMap.addMarker(MarkerOptions().position(position).title(location.title))
            newMarker?.tag = location
        }
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker): Boolean {

        val dialog = setNewDialog(p0)

        dialog?.show()

        return false
    }

    private fun setNewDialog(p0: Marker): Dialog {

        val loc = p0.tag as database.Location

        val inflater = layoutInflater;

        val dialog = Dialog(this)

        dialog.setContentView(inflater.inflate(R.layout.dialog, null))

        // setting up dialog data
        dialog.findViewById<TextView>(R.id.dialogTitle)?.text = loc.title

        dialog.findViewById<TextView>(R.id.dialogDescription)?.text = loc.description

        //setting up dialog buttons

        dialog.findViewById<Button>(R.id.dialogDismissButton).setOnClickListener {
            dialog.dismiss()
        }

        val spinner = dialog.findViewById<Spinner>(R.id.dialogSpinner)

        val adapter = ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories.toList())
        spinner?.adapter = adapter
        spinner?.onItemSelectedListener = this


        return dialog
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val value = parent.getItemAtPosition(position) as Category
        categoryId = value.id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}