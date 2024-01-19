package com.mypoi.category


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypoi.R
import com.example.mypoi.databinding.AddCategoryBinding
import database.Category
import database.Database
import database.LocationContract

class AddCategoryActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(AddCategoryBinding.inflate(layoutInflater).root)
        val dbHelper = Database(this)
        val writeDb = dbHelper.writableDatabase
        val readDib = dbHelper.readableDatabase
        val categories = dbHelper.getCategories(readDib)
        val button = findViewById<Button>(R.id.addButton)
        val categoriesList = findViewById<RecyclerView>(R.id.categoriesList)
        val adapter = CategoryAdapter(categories)
        categoriesList.adapter = adapter
        val newCat = Category()
        newCat.title = "Prova"
        button.setOnClickListener {
            dbHelper.addCategory(writeDb, newCat)
        }
    }
}

