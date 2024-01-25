package com.mypoi.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.startActivity
import com.example.mypoi.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mypoi.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mypoi.category.CategoryActivity
import com.mypoi.location.AddLocationActivity
import database.Category
import database.Database
import utils.Utils
import kotlin.system.exitProcess

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentLocation: Location? = null
    private val dbHelper = Database(this)
    private var categories = ArrayList<Category>()
    private var categoryId: Int = 0

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
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
        // custom back button to close applications
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MapsActivity().finish()

                exitProcess(0)
            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        currentLocation = Utils.getLocation(this, this)
        currentLocation = Utils.currentLocation
        // Add a marker in Sydney and move the camera
        var home = currentLocation?.let { LatLng(currentLocation!!.latitude, it.longitude ) }
        // if current location is not defined set default Uninsubria Monte Generoso
        if(home == null)
            home = LatLng(45.79, 8.85)
        home?.let { CameraUpdateFactory.newLatLng(it) }?.let { mMap.moveCamera(it) }

        val locations = dbHelper.getLocations(dbHelper.writableDatabase)

        // populate map using locations data from db
        for(location in locations) {
            val position = LatLng(location.x.toDouble(), location.y.toDouble())

            val newMarker = mMap.addMarker(MarkerOptions().
            position(position).title(location.title).icon(markerColor(location.category)))
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

        categoryId = loc.category

        val inflater = layoutInflater;

        val dialog = Dialog(this)

        dialog.setContentView(inflater.inflate(R.layout.dialog, null))

        // setting up dialog data
        val title = dialog.findViewById<TextView>(R.id.dialogTitle)

        title.text = loc.title

        val description = dialog.findViewById<TextView>(R.id.dialogDescription)

        description.text = loc.description

        // setting up spinner
        val spinner = dialog.findViewById<Spinner>(R.id.dialogSpinner)

        // selected category get moved on top of the array to be shown as default in the spinner
        val adapter = ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, moveCategoryOnTop(categories, loc.category).toList())
        spinner?.adapter = adapter
        spinner?.onItemSelectedListener = this

        //setting up dialog buttons

        dialog.findViewById<Button>(R.id.dialogDismissButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.dialogSaveButton).setOnClickListener {
            loc.title = title.text.toString()
            loc.description = description.text.toString()
            loc.category = categoryId
            // new data get saved as marker tag
            p0.tag = loc
            // marker gets its category color
            p0.setIcon(markerColor(loc.category))
            // db call
            dbHelper.updateLocations(dbHelper.writableDatabase, loc)
            // close dialog
            dialog.dismiss()
        }

        dialog.findViewById<FloatingActionButton>(R.id.dialogDeleteButton).setOnClickListener{
            // remove marker
            p0.remove()
            // db call
            dbHelper.deleteLocation(dbHelper.writableDatabase, loc.id)
            // close dialog
            dialog.dismiss()
        }


        return dialog
    }

    // method that moves selected category on top of categories array
    private fun moveCategoryOnTop (array: ArrayList<Category>, categoryId: Int): ArrayList<Category> {
        for (category in array){
            if(category.id == categoryId){
                array.remove(category)
                array.add(0, category)
                break
            }
        }

        return array
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val value = parent.getItemAtPosition(position) as Category
        categoryId = value.id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // map 10 different colors starting from an Id
    fun markerColor(id: Int): BitmapDescriptor {

        val normalized = id%10 + 1

        return BitmapDescriptorFactory.defaultMarker((normalized * 36).toFloat())

    }



}