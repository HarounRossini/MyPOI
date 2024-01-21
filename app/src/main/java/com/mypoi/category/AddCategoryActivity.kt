package com.mypoi.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.mypoi.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.Category
import database.Database

class AddCategoryActivity : AppCompatActivity() {

    private val dbHelper = Database(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val writeDb = dbHelper.writableDatabase
        setContentView(R.layout.activity_add_category)
        val addButton = findViewById<FloatingActionButton>(R.id.addCategoryButton)
        addButton.setOnClickListener {
            val newCat: Category = Category()
            newCat.title = findViewById<EditText>(R.id.categoryTitle).text.toString()
            dbHelper.addCategory(writeDb, newCat)
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

    }
}