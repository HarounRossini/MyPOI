package com.mypoi.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.mypoi.R
import database.Category
import database.Database

class AddLocationActivity : AppCompatActivity() {
    var dbHelper = Database(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        val writeDb = dbHelper.writableDatabase
        val readDib = dbHelper.readableDatabase
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_location)
        // get categories from db
        val categories = dbHelper.getCategories(readDib)

        val spinner = findViewById<Spinner>(R.id.categorySpinner)

        val adapter = ArrayAdapter<Category>


    }
}