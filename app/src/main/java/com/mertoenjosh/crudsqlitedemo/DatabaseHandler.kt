package com.mertoenjosh.crudsqlitedemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Creating database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmployeeDatabase"
        private const val TABLE_CONTACTS = "EmployeeTable"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // creating table with fields
        // "CREATE TABLE " + TABLE_CONTACTS + "("  + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT" + ")"
        val CREATE_CONTACTS_TABLE = (
                "CREATE TABLE $TABLE_CONTACTS($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_EMAIL TEXT)"
                )

        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    // INSERT data to the DB
    fun addEmployee(emp: EmpModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)

        // Inserting a Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close() // close the db connection

        // if (success == -1) false else true
        return success
    }

    // READ data from the table
    fun viewEmployee(): ArrayList<EmpModel> {
        val employeeList = ArrayList<EmpModel>()

        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) {
            do {
//                id = cursor.getInt(cursor.getColumnIndex("_id"))
                id = cursor.getInt(0)
                name = cursor.getString(1)
                email = cursor.getString(2)

                val emp = EmpModel(id, name, email)
                employeeList.add(emp)
            } while (cursor.moveToNext())
        }

        return employeeList
    }

    // UPDATE data in the database
    fun updateEmployee(emp: EmpModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, "$KEY_ID = ${emp.id}", null)

        db.close() // close db connection
        return success
    }

    // DELETE data from the database
    fun deleteEmployee(emp: EmpModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, emp.id)

        val success = db.delete(TABLE_CONTACTS, "$KEY_ID == ${emp.id}", null)

        db.close()
        return success
    }
}
