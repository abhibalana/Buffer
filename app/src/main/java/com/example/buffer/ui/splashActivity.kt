package com.example.buffer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.buffer.R
import com.example.buffer.authentication.LoginActivity
import com.example.buffer.authentication.welcomeActivity
import com.example.buffer.helper.SharedPrefrenceService

class splashActivity : AppCompatActivity() {
    private lateinit var pref : SharedPrefrenceService
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.hide()
        pref = SharedPrefrenceService()
        pref.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
         if(pref.read("FirstLogin","Abhi")=="yes"){
             startActivity(Intent(this,LoginActivity::class.java))
             finish()
         }
            else{
                pref.write("FirstLogin","yes")
             startActivity(Intent(this,welcomeActivity::class.java))
             finish()
         }


        },3000)
    }
}