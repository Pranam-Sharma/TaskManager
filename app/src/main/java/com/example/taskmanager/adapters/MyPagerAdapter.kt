package com.example.taskmanager.adapters


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.taskmanager.ui.notes.HomeFragment
import com.example.taskmanager.ui.notes.news.EntertainmentFragment
import com.example.taskmanager.ui.notes.news.PoliticsFragment

import com.example.taskmanager.ui.notes.news.SportFragment
import com.example.taskmanager.ui.notes.news.businessFragment

class MyPagerAdapter(
    fm: FragmentManager,
    private var totalTabs: Int,
    context: Context
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SportFragment()
            1 -> EntertainmentFragment()
            2 -> businessFragment()
            3-> PoliticsFragment()

            else -> HomeFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Tab" + (position + 1)
    }

}
