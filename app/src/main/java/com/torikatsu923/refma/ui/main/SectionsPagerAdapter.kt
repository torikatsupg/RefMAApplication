package com.torikatsu923.refma.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.torikatsu923.refma.GraphFragment
import com.torikatsu923.refma.ItemListFragment
import com.torikatsu923.refma.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3,
    R.string.tab_text_4
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> ItemListFragment.newInstance(0)
            1 -> ItemListFragment.newInstance(1)
            2 -> ItemListFragment.newInstance(2)
            else -> GraphFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 4 total pages.
        return TAB_TITLES.size
    }
}