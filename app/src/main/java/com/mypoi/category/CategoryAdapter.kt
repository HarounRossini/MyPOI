package com.mypoi.category

import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mypoi.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import database.Category
import database.Database

class CategoryAdapter(private val dataSet: ArrayList<Category>, private val db: Database): RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        lateinit var saveEditButton: FloatingActionButton
        lateinit var deleteButton: FloatingActionButton


        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.category_title)
            deleteButton = view.findViewById(R.id.deleteItemButton)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.category_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position].title

        // set up delete row buttons
        viewHolder.deleteButton.setOnClickListener {
            db.deleteCategory(db.writableDatabase, dataSet[position].id)
        }

    }
    override fun getItemCount() = dataSet.size


}