package com.torikatsu923.refma

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import org.w3c.dom.Text

class EditDataActivity : AppCompatActivity() {
    private var _primaryKey: Long = 0L
    private lateinit var _helper: SQLiteOpenHelper
    private lateinit var _csGetter: GetColumnItem
    private lateinit var _caster: CastCalendarType
    private lateinit var tvName:TextView
    private lateinit var tvDead: TextView
    private lateinit var tvBought: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvVolume: TextView
    private lateinit var tvRemnant: TextView
    private lateinit var etUse: EditText
    private lateinit var ivIcon: ImageView
    
    private var iconTable = arrayOf(
        R.drawable.ic_0, R.drawable.ic_1, R.drawable.ic_2, R.drawable.ic_3, R.drawable.ic_4,
        R.drawable.ic_5, R.drawable.ic_6, R.drawable.ic_7, R.drawable.ic_8, R.drawable.ic_9,
        R.drawable.ic_10, R.drawable.ic_11, R.drawable.ic_12, R.drawable.ic_13, R.drawable.ic_14,
        R.drawable.ic_15, R.drawable.ic_16, R.drawable.ic_17, R.drawable.ic_18, R.drawable.ic_19,
        R.drawable.ic_20, R.drawable.ic_21, R.drawable.ic_22, R.drawable.ic_23, R.drawable.ic_24,
        R.drawable.ic_25, R.drawable.ic_26, R.drawable.ic_27, R.drawable.ic_28, R.drawable.ic_29, R.drawable.ic_30
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_data)
        assignView()
        _primaryKey = intent.getLongExtra("primaryKey", 0L)
        _helper = DatabaseHelper(applicationContext)
        _csGetter = GetColumnItem()
        _caster = CastCalendarType()
        val db = _helper.writableDatabase
        val sql = "SELECT * FROM refrigerator WHERE _id =$_primaryKey"
        val cursor = db.rawQuery(sql, null)
        cursor.moveToFirst()
        tvName.text = _csGetter.getStr("name", cursor)
        tvDead.text = _caster.longToFormat(_csGetter.getLon("deadline", cursor))
        tvBought.text = _caster.longToFormat(_csGetter.getLon("boughtDay", cursor))
        tvPrice.text = _csGetter.getLonByStr("price", cursor)
        tvVolume.text = _csGetter.getLonByStr("volume", cursor)
        tvRemnant.text = _csGetter.getLonByStr("remnant", cursor)
        val position = _csGetter.getLon("iconImage", cursor).toInt()
        ivIcon.setImageResource(iconTable[position])
        //リスナ登録
        findViewById<Button>(R.id.btDump).setOnClickListener(ButtonClickListener())
        findViewById<Button>(R.id.btUse).setOnClickListener(ButtonClickListener())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _helper.close()
    }

    private fun assignView () {
        tvName = findViewById(R.id.tvDataName)
        tvDead = findViewById(R.id.tvDataDeadline)
        tvBought = findViewById(R.id.tvDataBoughtDay)
        tvPrice = findViewById(R.id.tvDataPrice)
        tvVolume = findViewById(R.id.tvDataVolume)
        tvRemnant = findViewById(R.id.tvDataRemnant)
        ivIcon = findViewById(R.id.ivIcon)
        etUse = findViewById(R.id.etUse)
    }

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            when(v.id) {
                R.id.btDump -> {
                    val dialog = DeleteCheckDialogFragment.newInstance(_primaryKey, true)
                    dialog.show(supportFragmentManager, "DeleteCheckDialogFragment")
                }
                R.id.btUse -> {
                    onBtUseClick()
                }
            }
        }
    }

    fun onBtUseClick() {
        if(etUse.length() != 0) {
            val useStr = etUse.text.toString()
            val use = useStr.toLong()
            val remnantStr = findViewById<TextView>(R.id.tvDataRemnant).text.toString()
            val remnant =  remnantStr.toLong()
            val amount = remnant - use
            val db = _helper.writableDatabase
            if(use != -1L && (amount) > 0) {
                val sql = "UPDATE refrigerator SET remnant=? WHERE _id=?"
                val stmt = db.compileStatement(sql)
                stmt.bindLong(1, amount)
                stmt.bindLong(2, _primaryKey)
                stmt.execute()
                finish()
            } else if(use != -1L && amount == 0L) {
                val sql = "DELETE FROM refrigerator WHERE _id = ?"
                val stmt = db.compileStatement(sql)
                stmt.bindLong(1, _primaryKey)
                stmt.executeUpdateDelete()
                Toast.makeText(applicationContext, "食品を使い切りました！", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, "使用量が不正です", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menuEdit -> {
                val intent = Intent(applicationContext, AddDataActivity::class.java)
                intent.putExtra("primaryKey", _primaryKey)
                intent.putExtra("isEditMode", true)
                startActivity(intent)
                finish()
            }
            R.id.menuDelete -> {
                val dialog = DeleteCheckDialogFragment.newInstance(_primaryKey, false)
                dialog.show(supportFragmentManager, "DeleteCheckDialogFragment")
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }
}