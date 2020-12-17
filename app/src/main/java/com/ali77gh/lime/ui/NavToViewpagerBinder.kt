package com.ali77gh.lime.ui

import androidx.viewpager.widget.ViewPager
import com.ali77gh.lime.R
import com.ali77gh.lime.ui.activity.MainActivity
import com.ali77gh.lime.ui.fragment.Backable
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavToViewpagerBinder {

    companion object{
        fun bind(navigation: BottomNavigationView, viewpager: ViewPager, adapter: ViewPagerAdapter) {
            val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.navigation_setting -> {
                            viewpager.setCurrentItem(0, true)
                            MainActivity.currentFrag = adapter.getItem(0) as Backable
                        }
                        R.id.navigation_event -> {
                            viewpager.setCurrentItem(1, true)
                            MainActivity.currentFrag = adapter.getItem(1) as Backable
                        }
                        R.id.navigation_my_day -> {
                            viewpager.setCurrentItem(2, true)
                            MainActivity.currentFrag = adapter.getItem(2) as Backable
                        }
                        R.id.navigation_task -> {
                            viewpager.setCurrentItem(3, true)
                            MainActivity.currentFrag = adapter.getItem(3) as Backable
                        }
                        R.id.navigation_routine -> {
                            viewpager.setCurrentItem(4, true)
                            MainActivity.currentFrag = adapter.getItem(4) as Backable
                        }
                        else -> throw RuntimeException("invalid menu")
                    }
                    true
                }
            val onPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
                override fun onPageSelected(i: Int) {
                    when (i) {
                        0 -> navigation.selectedItemId = R.id.navigation_setting
                        1 -> navigation.selectedItemId = R.id.navigation_event
                        2 -> navigation.selectedItemId = R.id.navigation_my_day
                        3 -> navigation.selectedItemId = R.id.navigation_task
                        4 -> navigation.selectedItemId = R.id.navigation_routine
                        else -> throw IllegalArgumentException("invalid page")
                    }
                    MainActivity.currentFrag = adapter.getItem(i) as Backable
                }

                override fun onPageScrollStateChanged(i: Int) {}
            }
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            viewpager.addOnPageChangeListener(onPageChangeListener)
        }
    }

}