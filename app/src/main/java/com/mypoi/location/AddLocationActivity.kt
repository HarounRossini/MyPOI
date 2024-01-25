package com.mypoi.location

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mypoi.main.MapsActivity
import com.example.mypoi.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.Category
import database.Database
import utils.Utils


class AddLocationActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var dbHelper = Database(this)

    private var currentLocation: Location? = null
    private var categoryId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        // get db
        val readDib = dbHelper.readableDatabase

        currentLocation = Utils.getLocation(this, this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        // get categories from db
        val categories = dbHelper.getCategories(readDib)

        // define spinner and its adapter to show categories
        val spinner = findViewById<Spinner>(R.id.categorySpinner)

        //if at least one category take first category Id as default categoryId
        if(categories.size > 0)
            categoryId = categories[0].id

        val adapter = ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categories.toList())
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        //define button

        findViewById<FloatingActionButton>(R.id.saveLocation).setOnClickListener {handleAdd()}

        //define x and y labels
        findViewById<TextView>(R.id.xEdit).text = currentLocation?.latitude?.toInt().toString()
        findViewById<TextView>(R.id.yEdit).text = currentLocation?.longitude?.toInt().toString()
    }

    private fun handleAdd(){
        val newLoc = database.Location()
        val writeDb = dbHelper.writableDatabase
        newLoc.x = findViewById<EditText>(R.id.xEdit).text.toString().toInt()
        newLoc.y = findViewById<EditText>(R.id.yEdit).text.toString().toInt()
        newLoc.title = findViewById<EditText>(R.id.locationTitle).text.toString()
        newLoc.description = findViewById<EditText>(R.id.description).text.toString()
        newLoc.category = categoryId
        dbHelper.addLocation(writeDb, newLoc)

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val value = parent.getItemAtPosition(position) as Category
        categoryId = value.id
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}



