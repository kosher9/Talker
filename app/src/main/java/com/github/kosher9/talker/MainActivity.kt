package com.github.kosher9.talker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.kosher9.talker.fragment.ChatFragment
import com.github.kosher9.talker.fragment.ContactsFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(ChatFragment(), "Chat")
        viewPagerAdapter.addFragment(ContactsFragment(), "Contacts")

        viewPager.adapter = viewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)

    }


    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        val mFragmentItems: ArrayList<Fragment> = ArrayList()
        val mFragmentTitles: ArrayList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return mFragmentItems[position]
        }

        override fun getCount(): Int {
            return mFragmentItems.size
        }

        fun addFragment(fragmentItem: Fragment, fragmentTitle: String){
            mFragmentItems.add(fragmentItem)
            mFragmentTitles.add(fragmentTitle)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitles[position]
        }

    }

}
