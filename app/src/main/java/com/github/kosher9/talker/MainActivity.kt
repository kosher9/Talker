package com.github.kosher9.talker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.github.kosher9.talker.fragment.ChatFragment
import com.github.kosher9.talker.fragment.ContactsFragment
import com.github.kosher9.talker.model.User
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var profilImage: CircleImageView
    private lateinit var reference: DatabaseReference
    private lateinit var fuser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        profilImage = findViewById(R.id.user_profile)
        fuser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.uid)

        viewPagerAdapter.addFragment(ChatFragment(), "Chat")
        viewPagerAdapter.addFragment(ContactsFragment(), "Contacts")

        viewPager.adapter = viewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        profilImage.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                return
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                Glide.with(this@MainActivity).load(user?.profilUrl).into(profilImage)
            }

        })

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
