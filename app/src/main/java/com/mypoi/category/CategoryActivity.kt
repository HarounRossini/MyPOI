package com.mypoi.category


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.mypoi.R
import com.example.mypoi.databinding.ActivityCategoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mypoi.main.MapsActivity
import database.Database

class CategoryActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityCategoryBinding.inflate(layoutInflater).root)
        // defining database utils
        val dbHelper = Database(this)
        val readDib = dbHelper.readableDatabase
        //fetching categories and populating list view
        val categories = dbHelper.getCategories(readDib)
        val categoriesList = findViewById<RecyclerView>(R.id.categoriesList)
        val adapter = CategoryAdapter(categories, dbHelper)
        categoriesList.adapter = adapter
        Log.d("categorie", categories.toString())
        //setting up button to open AddCategoryActivity
        val button = findViewById<FloatingActionButton>(R.id.addButton)
        button.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        // custom back button to point MapActivity
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(applicationContext, MapsActivity::class.java))
            }
        })


    }


}

