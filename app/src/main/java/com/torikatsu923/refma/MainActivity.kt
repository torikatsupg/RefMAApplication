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
    private lateinit var fab: FloatingActionButton
    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(applicationContext, supportFragmentManager)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        tabs = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        fab = findViewById(R.id.fab)
        //fabリスナ
        fab.setOnClickListener { onFabButtonClick() }
    }

    override fun onRestart() {
        super.onRestart()
        viewPager.adapter = SectionsPagerAdapter(applicationContext, supportFragmentManager)
    }

    private fun onFabButtonClick() {
        val intent = Intent(applicationContext, AddDataActivity::class.java)
        startActivity(intent)
    }


}