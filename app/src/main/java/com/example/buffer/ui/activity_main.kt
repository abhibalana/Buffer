package com.example.buffer.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.buffer.R
import com.example.buffer.fragmnets.fragment_home
import com.example.buffer.fragmnets.fragment_library
import com.example.buffer.fragmnets.fragment_search
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val homeFragment = fragment_home()
        val searchFragment = fragment_search()
        val settingsFragment = fragment_library()
        setCurrentFragment(homeFragment)
        bottom_navigation_bar.elevation=0f
        bottom_navigation_bar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Home->{
                    supportActionBar!!.hide()
                    setCurrentFragment(homeFragment)
                }
                R.id.search->setCurrentFragment(searchFragment)
                R.id.library->{
                    supportActionBar!!.hide()
                    setCurrentFragment(settingsFragment)
                }

            }
            true
        }
        }




    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainframe,fragment)
            commit()
        }



}