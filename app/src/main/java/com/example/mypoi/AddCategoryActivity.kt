package com.example.mypoi


import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.mypoi.databinding.AddCategoryBinding
import database.Category
import database.Database
import database.LocationContract

class AddCategoryActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(AddCategoryBinding.inflate(layoutInflater).root)
        var dbHelper = Database(this)
        var writeDb = dbHelper.writableDatabase
        var readDib = dbHelper.readableDatabase
         val button = findViewById<Button>(R.id.addButton)
        val newCat = Category()
        newCat.title = "Prova"
        button.setOnClickListener {
            dbHelper.addCategory(writeDb, newCat)
        }
        val cursor = dbHelper.getCategories(readDib)
        with(cursor) {
            while (this!!.moveToNext()) {
                cursor?.getString(cursor.getColumnIndexOrThrow(LocationContract.Category.COLUMN_NAME_TITLE))
                    ?.let { Log.d("dati", it) }
            }
        }
    }
}

