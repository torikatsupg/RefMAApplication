package com.torikatsu923.refma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View

class ChooseIconActivity : AppCompatActivity() {
    private var _iconTablePosition: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chose_icon)
        _iconTablePosition = intent.getIntExtra("iconTablePosition", 1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPause() {
        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        pref.edit().putInt("iconTablePosition", _iconTablePosition).commit()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    fun onImageClick(v: View) {
        when(v.id) {
            R.id.ic1 -> _iconTablePosition = 1
            R.id.ic2 -> _iconTablePosition = 2
            R.id.ic3 -> _iconTablePosition = 3
            R.id.ic4 -> _iconTablePosition = 4
            R.id.ic5 -> _iconTablePosition = 5
            R.id.ic6 -> _iconTablePosition = 6
            R.id.ic7 -> _iconTablePosition = 7
            R.id.ic8 -> _iconTablePosition = 8
            R.id.ic9 -> _iconTablePosition = 9
            R.id.ic10 -> _iconTablePosition = 10
            R.id.ic11 -> _iconTablePosition = 11
            R.id.ic12 -> _iconTablePosition = 12
            R.id.ic13 -> _iconTablePosition = 13
            R.id.ic14 -> _iconTablePosition = 14
            R.id.ic15 -> _iconTablePosition = 15
            R.id.ic16 -> _iconTablePosition = 16
            R.id.ic17 -> _iconTablePosition = 17
            R.id.ic18 -> _iconTablePosition = 18
            R.id.ic19 -> _iconTablePosition = 19
            R.id.ic20 -> _iconTablePosition = 20
            R.id.ic21 -> _iconTablePosition = 21
            R.id.ic22 -> _iconTablePosition = 22
            R.id.ic23 -> _iconTablePosition = 23
            R.id.ic24 -> _iconTablePosition = 24
            R.id.ic25 -> _iconTablePosition = 25
            R.id.ic26 -> _iconTablePosition = 26
            R.id.ic27 -> _iconTablePosition = 27
            R.id.ic28 -> _iconTablePosition = 28
            R.id.ic29 -> _iconTablePosition = 29
            R.id.ic30 -> _iconTablePosition = 30
        }
        finish()
    }
}
