package com.mypoi.category


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mypoi.R
import com.example.mypoi.databinding.AddCategoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.Category
import database.Database

class CategoryActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(AddCategoryBinding.inflate(layoutInflater).root)
        // defining database utils
        val dbHelper = Database(this)
        val writeDb = dbHelper.writableDatabase
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
    }
}

