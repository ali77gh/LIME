package com.ali77gh.lime.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.ali77gh.lime.R
import com.ali77gh.lime.ui.NavToViewpagerBinder
import com.ali77gh.lime.ui.ViewPagerAdapter
import com.ali77gh.lime.ui.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var viewPagerAdapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
    var currentFrag: Backable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNav()
    }

    private fun setupNav() {

        val settingsFragment = SettingsFragment()
        val eventFragment = EventFragment()
        val myDayFragment = MyDayFragment()
        val taskFragment = TaskFragment()
        val routineFragment = RoutineFragment()

        viewPagerAdapter.addFragment(settingsFragment)
        viewPagerAdapter.addFragment(eventFragment)
        viewPagerAdapter.addFragment(myDayFragment)
        viewPagerAdapter.addFragment(taskFragment)
        viewPagerAdapter.addFragment(routineFragment)

        NavToViewpagerBinder.bind(navigation_home, viewpager_home, viewPagerAdapter)
        viewpager_home.post { viewpager_home.setCurrentItem(DEFAULT_NAV_MENU, false) }
        navigation_home.selectedItemId = R.id.navigation_my_day
        currentFrag = myDayFragment
        viewpager_home.adapter = viewPagerAdapter
    }

    companion object{
        var currentFrag: Backable?=null
        private const val DEFAULT_NAV_MENU = 2
    }
}