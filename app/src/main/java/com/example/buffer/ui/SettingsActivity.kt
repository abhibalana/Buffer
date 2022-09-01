package com.example.buffer.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.buffer.R
import com.example.buffer.fragmnets.fragment_settings
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = "Settings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_color))
        supportActionBar!!.elevation=0f
        val user = auth.currentUser
        Glide.with(this).load(user!!.photoUrl).circleCrop().into(userImage)
        username.text=user.displayName
        fragmentManager.beginTransaction().add(R.id.settings_framelayout,fragment_settings()).commit()


    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}