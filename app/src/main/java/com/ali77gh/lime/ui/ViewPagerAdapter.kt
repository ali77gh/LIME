package com.ali77gh.lime.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val _mFragmentList: MutableList<Fragment> = ArrayList()
    private val _mFragmentTitles: MutableList<String> = ArrayList()

    override  fun getItem(position: Int): Fragment {
        return _mFragmentList[position]
    }

    override fun getCount(): Int {
        return _mFragmentList.size
    }

    override  fun getPageTitle(position: Int): CharSequence? {
        return _mFragmentTitles[position]
    }

    fun addFragment(fragment: Fragment) {
        _mFragmentList.add(fragment)
        _mFragmentTitles.add("")
    }

    fun addFragment(fragment: Fragment, title: String) {
        _mFragmentList.add(fragment)
        _mFragmentTitles.add(title)
    }
}