package com.example.buffer.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import com.example.buffer.R
import com.example.buffer.helper.constant
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_webview.*

class webviewActivity : AppCompatActivity() {
    private lateinit var url:String
    @SuppressLint("SetJavaScriptEnabled", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        supportActionBar!!.title = intent.getStringExtra("Privacy").toString()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_color))
        supportActionBar!!.elevation=0f
        if(intent.getStringExtra("Privacy")=="Terms&Conditions"){
            url = constant.termsconditionurl
        }
        else{
            url=constant.privacy
        }

        webview.loadUrl(url)
        webview.settings.javaScriptEnabled=true
        webview.webViewClient = WebViewClient()
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