package com.torikatsu923.refma


import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import java.util.*

class AddDataActivity : AppCompatActivity(), DatePickerDialogFragment.OnDateSelectedListener{
    private var _isEditMode = false
    private var _primaryKey = 0L
    private var _iconTablePosition = 1
    private var _iconTable = arrayOf(
        R.drawable.ic_0, R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4,
        R.drawable.ic_5, R.drawable.ic_6, R.drawable.ic_7, R.drawable.ic_8, R.drawable.ic_9,
        R.drawable.ic_10, R.drawable.ic_11, R.drawable.ic_12, R.drawable.ic_13, R.drawable.ic_14,
        R.drawable.ic_15, R.drawable.ic_16, R.drawable.ic_17, R.drawable.ic_18, R.drawable.ic_19,
        R.drawable.ic_20, R.drawable.ic_21, R.drawable.ic_22, R.drawable.ic_23, R.drawable.ic_24,
        R.drawable.ic_25, R.drawable.ic_26, R.drawable.ic_27, R.drawable.ic_28, R.drawable.ic_29, R.drawable.ic_30
    )
    private lateinit var _helper: SQLiteOpenHelper
    private lateinit var _caster: CastCalendarType
    private lateinit var _csGetter : GetColumnItem
    private lateinit var nameView : EditText
    private lateinit var deadView: TextView
    private lateinit var boughtView: TextView
    private lateinit var priceView: EditText
    private lateinit var volumeView: EditText
    private lateinit var remnantView: EditText
    private lateinit var ivIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)
        _isEditMode = intent.getBooleanExtra("isEditMode", false)
        _primaryKey = intent.getLongExtra("primaryKey", 0L)
        _helper = DatabaseHelper(applicationContext)
        _caster = CastCalendarType()
        _csGetter = GetColumnItem()
        assignView()
        initView()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //ビューの割り当て
    private fun assignView () {
        nameView = findViewById(R.id.etName)
        deadView = findViewById(R.id.tvDeadline)
        boughtView = findViewById(R.id.tvBoughtDay)
        priceView =  findViewById(R.id.etPrice)
        volumeView = findViewById(R.id.etVolume)
        remnantView = findViewById(R.id.etRemnant)
        ivIcon = findViewById(R.id.ivIcon)
    }

    //各ビューの初期化
    private fun initView () {
        if(_isEditMode && _primaryKey != 0L) {
            val db = _helper.writableDatabase
            val sql = "SELECT * FROM refrigerator WHERE _id = $_primaryKey "
            val cursor = db.rawQuery(sql, null)
            cursor.moveToFirst()
            nameView.setText(_csGetter.getStr("name", cursor))
            deadView.text = _caster.strToFormat(_csGetter.getStr("deadline", cursor))
            boughtView.text = _caster.strToFormat(_csGetter.getStr("boughtDay", cursor))
            priceView.setText(_csGetter.getLonByStr("price", cursor))
            volumeView.setText(_csGetter.getLonByStr("volume", cursor))
            remnantView.setText(_csGetter.getLonByStr("remnant", cursor))
            _iconTablePosition = _csGetter.getLon("iconImage", cursor).toInt()
            ivIcon.setImageResource(_iconTable[_iconTablePosition])
        }else { 
            deadView.text = _caster.getNowFormat()
            boughtView.text = _caster.getNowFormat()
        }
    }

    private fun showToast (msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        _iconTablePosition = pref.getInt("iconTablePosition", 1)
        ivIcon.setImageResource(_iconTable[_iconTablePosition])
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    //日付選択ダイアログの選択リスナ
    override fun onSelected(year: Int, month: Int, date: Int, isTvDeadline: Boolean) {
        val c = Calendar.getInstance()
        c.set(year, month, date)
        when(isTvDeadline) {
            true -> deadView.text = _caster.calToFormat(c)
            false -> boughtView.text = _caster.calToFormat(c)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    fun onIvIconClick(view: View) {
        val intent = Intent(applicationContext, ChooseIconActivity::class.java)
        intent.putExtra("iconTablePosition", _iconTablePosition)
        startActivity(intent)
    }
    
    fun onTvEditDateClick(view: View) {
        var isTvDeadline = false
        when(view.id) {
            R.id.tvDeadline -> isTvDeadline = true
            R.id.tvBoughtDay -> isTvDeadline = false
        }
        DatePickerDialogFragment(isTvDeadline).show(supportFragmentManager, "deadlineDialog")
    }

    fun onSaveButtonClick(view: View) {
        //入力情報の取得
        val name = nameView.text.toString()
        val deadStr = deadView.text.toString()
        val boughtStr = boughtView.text.toString()
        val priceStr = priceView.text.toString()
        val volumeStr = volumeView.text.toString()
        val remnantStr = remnantView.text.toString()
        if(name.isNotEmpty() && deadStr.isNotEmpty() && boughtStr.isNotEmpty() && priceStr.isNotEmpty() && volumeStr.isNotEmpty() && remnantStr.isNotEmpty() ) {
            //入力内容が正しければ変換
            val dead = _caster.formatToLong(deadStr)
            val bought = _caster.formatToLong(boughtStr)
            val price = priceStr.toLong()
            val volume = volumeStr.toLong()
            val remnant = remnantStr.toLong()
            //SQL処理
            val db = _helper.writableDatabase
            if (volume - remnant < 0L) {
                showToast("入力内容が正しくありません。")
                return
            } else if (_isEditMode) {
                var sql = "UPDATE refrigerator SET name=?, deadline=?, boughtDay=?, price=?, volume=?, remnant=?, iconImage=? WHERE _id=?"
                var stmt = db.compileStatement(sql)
                stmt.bindString(1, name)
                stmt.bindLong(2, dead)
                stmt.bindLong(3, bought)
                stmt.bindLong(4, price)
                stmt.bindLong(5, volume)
                stmt.bindLong(6, remnant)
                stmt.bindLong(7, _iconTablePosition.toLong())
                stmt.bindLong(8, _primaryKey)
                stmt.execute()
                //IDが同じアイテムの購入情報を変更
                val updateSql = "UPDATE bought SET boughtDay=?, volume=? WHERE _id=?"
                stmt = db.compileStatement(updateSql)
                stmt.bindLong(1, bought)
                stmt.bindLong(2, volume)
                stmt.bindLong(3, _primaryKey)
                stmt.execute()
            } else if (!_isEditMode && volume - remnant >= 0) {
                val sql = "INSERT INTO bought (_id, boughtDay, volume) VALUES (?, ?, ?)"
                val stmt = db.compileStatement(sql)
                _primaryKey = _caster.getPrimaryKey()
                stmt.bindLong(1, _primaryKey)
                stmt.bindLong(2, bought)
                stmt.bindLong(3, volume)
                stmt.executeInsert()
            }
            if(remnant != 0L && !_isEditMode)  {
                //insertSQLの実行
                val sql =
                    "INSERT INTO refrigerator (name, deadline, boughtDay, price, volume, remnant, iconImage, _id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                val stmt = db.compileStatement(sql)
                stmt.bindString(1, name)
                stmt.bindLong(2, dead)
                stmt.bindLong(3, bought)
                stmt.bindLong(4, price)
                stmt.bindLong(5, volume)
                stmt.bindLong(6, remnant)
                stmt.bindLong(7, _iconTablePosition.toLong())
                stmt.bindLong(8, _primaryKey)
                stmt.executeInsert()
            } else if (remnant == 0L && !_isEditMode) {
                showToast("食品を使い切りました！")
            }
            finish()
        } else {
            showToast("入力内容に誤りがあります")
        }
    }
}
