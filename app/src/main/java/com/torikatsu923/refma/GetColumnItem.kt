package com.torikatsu923.refma

import android.database.Cursor

class GetColumnItem {
    fun getStr (key: String, cursor: Cursor) : String{
        return cursor.getString(cursor.getColumnIndex("$key"))
    }

    fun getLon (key: String, cursor: Cursor) : Long{
        return cursor.getLong(cursor.getColumnIndex("$key"))
    }

    fun getInt (key: String, cursor: Cursor) : Int{
        return cursor.getInt(cursor.getColumnIndex("$key"))
    }

    fun getLonByStr (key: String, cursor: Cursor) : String{
        return cursor.getLong(cursor.getColumnIndex("$key")).toString()
    }
}