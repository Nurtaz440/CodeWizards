package com.example.farganaapp.ui.custom

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBopenHelper(context:Context) : SQLiteOpenHelper(context,DBStructure.DB_NAME,null,DBStructure.DB_VERSION) {
    val CREATE_EVENTS_TABLE = "create table " + DBStructure.EVENT_TABLE_NAME +
            "(ID INTEGER  PRIMARY KEY AUTOINCREMENT, " +
            DBStructure.EVENT + " TEXT, " +
            DBStructure.TIME + " TEXT, " +
            DBStructure.DATE + " TEXT, " +
            DBStructure.MONTH + " TEXT, " +
            DBStructure.YEAR + " TEXT, " +
            DBStructure.Notify + " TEXT)"

    val DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS " + DBStructure.EVENT_TABLE_NAME

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_EVENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_EVENTS_TABLE)
        onCreate(db)
    }

    fun saveEvent(event : String, time:String, date:String, month : String, year : String, database : SQLiteDatabase){
        val contentValues = ContentValues()
        contentValues.put(DBStructure.EVENT,event)
        contentValues.put(DBStructure.TIME,time)
        contentValues.put(DBStructure.DATE,date)
        contentValues.put(DBStructure.MONTH,month)
        contentValues.put(DBStructure.YEAR,year)
        database.insert(DBStructure.EVENT_TABLE_NAME,null,contentValues)

    }

    fun readEvents( date : String, database: SQLiteDatabase) : Cursor{
        val Projections = arrayOf(DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR)
        val selection = DBStructure.DATE + "=?"
     val selectionArgs= arrayOf(date)
     return database.query(DBStructure.EVENT_TABLE_NAME,Projections,selection,selectionArgs,null,null,null)
    }
    fun readIdEvents(date : String,event : String, time : String, database: SQLiteDatabase) : Cursor{
        val Projections = arrayOf(DBStructure.ID, DBStructure.Notify)
        val selection = DBStructure.DATE +  "=? and "  + DBStructure.EVENT  + DBStructure.TIME + "=?"
        val selectionArgs= arrayOf(date,event,time)
     return database.query(DBStructure.EVENT_TABLE_NAME,Projections,selection,selectionArgs,null,null,null)
    }
     fun readEventsMonth( month : String,year : String, database: SQLiteDatabase) : Cursor{
        val Projections = arrayOf(DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR)
        val selection = DBStructure.MONTH + "=? and " + DBStructure.YEAR + "=?"
     val selectionArgs= arrayOf(month,year)
     return database.query(DBStructure.EVENT_TABLE_NAME,Projections,selection,selectionArgs,null,null,null)
    }
    fun deleteEvent(event : String, date : String,time : String, database: SQLiteDatabase){
        val selection = DBStructure.EVENT + "=? and " + DBStructure.DATE + "=? and " +  DBStructure.TIME+ "=?"
        val selectionArgs= arrayOf(event,date,time)
        database.delete(DBStructure.EVENT_TABLE_NAME,selection,selectionArgs)

    }

    fun updateEvent(date : String,event : String, time : String,notify : String, database: SQLiteDatabase){
        val contentValues = ContentValues()
        contentValues.put(DBStructure.Notify,notify)
        val selection = DBStructure.DATE +  "=? and "  + DBStructure.EVENT  + DBStructure.TIME + "=?"
        val selectionArgs= arrayOf(date,event,time)
        database.update(DBStructure.EVENT_TABLE_NAME,contentValues,selection,selectionArgs)
    }
}