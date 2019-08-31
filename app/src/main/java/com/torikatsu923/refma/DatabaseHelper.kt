package com.torikatsu923.refma

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.recyclerview.widget.RecyclerView
import java.lang.StringBuilder

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        //データベースファイル名の定数フィールド
        private const val DATABASE_NAME = "refrigerator.db"
        private const val DATABASE_VERSION = 1
    }

    /**
     * name:String：アイテム名
     * deadline:Int：賞味・消費期限 ミリ秒
     * boughtDay:Int：購入日　ミリ秒
     * price:Int：価格
     * volume:Int：量（グラム）
     * remnant:Int：残量（グラム）
     * iconImage:R：アイコン画像リソース値
     */
    override fun onCreate(db: SQLiteDatabase) {
        //冷蔵庫データベース
        var sb = StringBuilder()
        sb.append("CREATE TABLE refrigerator (")
        sb.append("_id LONG PRIMARY KEY,")
        sb.append("name TEXT,")
        sb.append("deadline LONG,")
        sb.append("boughtDay LONG,")
        sb.append("price LONG,")
        sb.append("volume LONG,")
        sb.append("remnant LONG,")
        sb.append("iconImage LONG")
        sb.append(");")
        var sql = sb.toString()
        db.execSQL(sql)
        //購入量データベース
        sb = StringBuilder()
        sb.append("CREATE TABLE bought (")
        sb.append("_id LONG PRIMARY KEY,")
        sb.append("boughtDay LONG,")
        sb.append("volume LONG")
        sb.append(");")
        sql = sb.toString()
        db.execSQL(sql)
        //廃棄データベース
        sb = StringBuilder()
        sb.append("CREATE TABLE dump (")
        sb.append("_id LONG PRIMARY KEY,")
        sb.append("dumpDay LONG,")
        sb.append("volume LONG")
        sb.append(");")
        sql = sb.toString()
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}