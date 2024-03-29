package com.torikatsu923.refma

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat


class DeleteCheckDialogFragment : DialogFragment() {
    private lateinit var _helper: SQLiteOpenHelper
    private lateinit var _caster: CastCalendarType
    private lateinit var  _csGetter: GetColumnItem
    private var _primaryKey = 0L
    private var _isDump = false

    companion object {
        fun newInstance(_primaryKey: Long, _isDump: Boolean) : DeleteCheckDialogFragment {
            val dialog = DeleteCheckDialogFragment()
            dialog._primaryKey = _primaryKey
            dialog._isDump = _isDump
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _helper = context?.let { DatabaseHelper(it) }!!
        _caster = CastCalendarType()
        _csGetter = GetColumnItem()
        val builder = AlertDialog.Builder(context)
        if(_isDump) {
            builder.setTitle(R.string.dialog_check_dump_title)
            builder.setMessage(R.string.dialog_check_dump_text)
            builder.setPositiveButton(R.string.dialog_check_dump_ok, DialogButtonClickListener())
        } else {
            builder.setTitle(R.string.dialog_check_delete_title)
            builder.setMessage(R.string.dialog_check_delete_text)
            builder.setPositiveButton(R.string.dialog_check_delete_ok, DialogButtonClickListener())
        }
        builder.setNegativeButton(R.string.dialog_check_delete_no, DialogButtonClickListener())
        return builder.create()
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    private inner class DialogButtonClickListener: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val db = _helper.writableDatabase
            if (which == DialogInterface.BUTTON_POSITIVE) {
                val selectSql = "SELECT * FROM refrigerator WHERE _id = $_primaryKey"
                val cursor = db.rawQuery(selectSql, null)
                cursor.moveToFirst()
                val remnant = _csGetter.getLon("remnant", cursor)
                val now = _caster.getNowLongRound()

                val sql = "DELETE FROM refrigerator WHERE _id = ?"
                var stmt = db.compileStatement(sql)
                stmt.bindLong(1, _primaryKey)
                stmt.executeUpdateDelete()

                //廃棄データに削除データを追加
                if(_isDump) {
                    val insertSql = "INSERT INTO dump (_id, dumpDay, volume) VALUES (?, ?, ?)"
                    stmt = db.compileStatement(insertSql)
                    stmt.bindLong(1, _primaryKey)
                    stmt.bindLong(2, now)
                    stmt.bindLong(3, remnant)
                    stmt.executeInsert()
                    Toast.makeText(context, "食品を廃棄しました", Toast.LENGTH_LONG).show()
                } else {
                    val deleteBouSql = "DELETE FROM bought WHERE _id=?"
                    stmt = db.compileStatement(deleteBouSql)
                    stmt.bindLong(1, _primaryKey)
                    stmt.executeUpdateDelete()

                    val deleteDumSql = "DELETE FROM dump WHERE _id=?"
                    stmt = db.compileStatement(deleteDumSql)
                    stmt.bindLong(1, _primaryKey)
                    stmt.executeUpdateDelete()

                    Toast.makeText(context, "データを削除しました", Toast.LENGTH_LONG).show()
                }
                activity?.finish()
            }
        }
    }
}
