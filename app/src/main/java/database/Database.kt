package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log


const val DATABASE_VERSION = 5
const val DATABASE_NAME = "MyPOI.db"



class Database (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    private val location = LocationContract.Location
    private val category = LocationContract.Category


    private val SQL_CREATE_LOCATIONS =
    "CREATE TABLE ${LocationContract.Location.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${LocationContract.Location.COLUMN_NAME_TITLE} TEXT NOT NULL," +
            "${LocationContract.Location.COLUMN_NAME_X} INTEGER NOT NULL," +
            "${LocationContract.Location.COLUMN_NAME_Y} INTEGER NOT NULL," +
            "${LocationContract.Location.COLUMN_NAME_DESCRIPTION} TEXT," +
            "${LocationContract.Location.COLUMN_NAME_CATEGORY} INTEGER NOT NULL" +
            " REFERENCES ${LocationContract.Category.TABLE_NAME} (${BaseColumns._ID}))"

    private val SQL_CREATE_CATEGORIES = "CREATE TABLE ${LocationContract.Category.TABLE_NAME}(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${LocationContract.Category.COLUMN_NAME_TITLE} TEXT NOT NULL)"

    private val SQL_DELETE_LOCATIONS = "DROP TABLE IF EXISTS ${LocationContract.Location.TABLE_NAME}"
    private val SQL_DELETE_CATEGORIES = "DROP TABLE IF EXISTS ${LocationContract.Category.TABLE_NAME}"


    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("db", SQL_CREATE_LOCATIONS)
        db?.execSQL(SQL_CREATE_CATEGORIES)
        db?.execSQL(SQL_CREATE_LOCATIONS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_LOCATIONS)
        db?.execSQL(SQL_DELETE_CATEGORIES)
        onCreate(db)
    }

    fun addCategory(db: SQLiteDatabase?, newCat: Category){
        var values = ContentValues()
        values.put(category.COLUMN_NAME_TITLE, newCat.title)
        db?.insert(category.TABLE_NAME, null, values)
    }

    fun addLocation(db: SQLiteDatabase?, newLoc: Location){
        var values = ContentValues()
        values.put(location.COLUMN_NAME_CATEGORY, newLoc.category)
        values.put(location.COLUMN_NAME_TITLE, newLoc.title)
        values.put(location.COLUMN_NAME_X, newLoc.x)
        values.put(location.COLUMN_NAME_Y, newLoc.y)
        values.put(location.COLUMN_NAME_DESCRIPTION, newLoc.description)
        db?.insert(location.TABLE_NAME, null, values)

    }

    fun getCategories(db: SQLiteDatabase?): ArrayList<Category> {
        val projection = arrayOf(BaseColumns._ID, category.COLUMN_NAME_TITLE)
        val resultArray = ArrayList<Category>()
        val cursor = db?.query(
            category.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (this!!.moveToNext()) {
                val rowCategory = Category()
                rowCategory.id = cursor?.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))!!
                rowCategory.title =
                    cursor?.getString(cursor.getColumnIndexOrThrow(category.COLUMN_NAME_TITLE))!!
                resultArray.add(rowCategory)

            }

            return resultArray

        }
    }

    fun getLocations(db: SQLiteDatabase?): ArrayList<Location> {
        val projection = arrayOf(BaseColumns._ID,
            location.COLUMN_NAME_TITLE,
            location.COLUMN_NAME_X,
            location.COLUMN_NAME_Y,
            location.COLUMN_NAME_DESCRIPTION,
            location.COLUMN_NAME_CATEGORY
        )
        val resultArray = ArrayList<Location>()
        val cursor = db?.query(
            location.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (this!!.moveToNext()) {
                val rowCategory = Location()
                rowCategory.id = cursor?.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))!!
                rowCategory.title =
                    cursor?.getString(cursor.getColumnIndexOrThrow(location.COLUMN_NAME_TITLE))!!
                rowCategory.description =
                    cursor?.getString(cursor.getColumnIndexOrThrow(location.COLUMN_NAME_DESCRIPTION))!!
                rowCategory.x =
                    cursor?.getInt(cursor.getColumnIndexOrThrow(location.COLUMN_NAME_X))!!
                rowCategory.y =
                    cursor?.getInt(cursor.getColumnIndexOrThrow(location.COLUMN_NAME_Y))!!
                rowCategory.category =
                    cursor?.getInt(cursor.getColumnIndexOrThrow(location.COLUMN_NAME_CATEGORY))!!

                resultArray.add(rowCategory)

            }

            return resultArray

        }
    }

    fun deleteCategory(db: SQLiteDatabase?, id: Int) {
        //val sql = "DELETE FROM " + category.TABLE_NAME + " WHERE " + BaseColumns._ID + " = " + id.toString()
        //db?.execSQL(sql)
        db?.delete(category.TABLE_NAME, BaseColumns._ID + " = ?", arrayOf(id.toString()))
    }

    fun deleteLocation(db: SQLiteDatabase?, id: Int) {
        db?.delete(location.TABLE_NAME, BaseColumns._ID + " = ?", arrayOf(id.toString()))
    }

    fun updateCategory(db: SQLiteDatabase?, updated: Category){
        val values = ContentValues()
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(updated.id.toString())

        values.put(category.COLUMN_NAME_TITLE, updated.title)
        db?.update(category.TABLE_NAME, values, selection, selectionArgs)
    }

    fun updateLocations(db: SQLiteDatabase?, updated: Location){
        val values = ContentValues()
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(updated.id.toString())

        values.put(location.COLUMN_NAME_TITLE, updated.title)
        values.put(location.COLUMN_NAME_DESCRIPTION, updated.description)
        values.put(location.COLUMN_NAME_CATEGORY, updated.category)

        db?.update(location.TABLE_NAME, values, selection, selectionArgs)
    }

}

object LocationContract {
    // Table contents are grouped together in an anonymous object.
    object Location : BaseColumns {
        const val TABLE_NAME = "location"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_X = "X"
        const val COLUMN_NAME_Y = "Y"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_CATEGORY = "category"
    }

    object Category: BaseColumns {
        const val TABLE_NAME = "category"
        const val COLUMN_NAME_TITLE = "title"
    }
}
