package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns



const val DATABASE_VERSION = 1
const val DATABASE_NAME = "MyPOI.db"



class Database (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    private val location = LocationContract.Location
    private val category = LocationContract.Category


    private val SQL_CREATE_LOCATIONS =
    "CREATE TABLE ${LocationContract.Location.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${LocationContract.Location.COLUMN_NAME_TITLE} TEXT," +
            "${LocationContract.Location.COLUMN_NAME_X} TEXT," +
            "${LocationContract.Location.COLUMN_NAME_Y} TEXT," +
            "${LocationContract.Location.COLUMN_NAME_DESCRIPTION} TEXT," +
            "${LocationContract.Location.COLUMN_NAME_CATEGORY} INTEGER)"

    private val SQL_CREATE_CATEGORIES = "CREATE TABLE ${LocationContract.Category.TABLE_NAME}(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${LocationContract.Category.COLUMN_NAME_TITLE} TEXT)"

    private val SQL_DELETE_LOCATIONS = "DROP TABLE IF EXISTS ${LocationContract.Location.TABLE_NAME}"
    private val SQL_DELETE_CATEGORIES = "DROP TABLE IF EXISTS ${LocationContract.Category.TABLE_NAME}"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_LOCATIONS)
        db?.execSQL(SQL_CREATE_CATEGORIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_LOCATIONS)
        db?.execSQL(SQL_DELETE_CATEGORIES)
    }

    fun addCategory(){

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
