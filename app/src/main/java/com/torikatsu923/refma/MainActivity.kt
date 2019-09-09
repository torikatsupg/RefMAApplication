package com.torikatsu923.refma

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.torikatsu923.refma.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        //fabリスナ
        fab.setOnClickListener { onFabButtonClick() }
        //TutorialActivity.showIfNeeded(this@MainActivity, savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = SectionsPagerAdapter(applicationContext, supportFragmentManager)
    }

    private fun onFabButtonClick() {
        val intent = Intent(applicationContext, AddDataActivity::class.java)
        startActivity(intent)
    }


}