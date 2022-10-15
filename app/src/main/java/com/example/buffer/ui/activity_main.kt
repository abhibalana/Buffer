package com.example.buffer.ui

import android.app.ActivityOptions
import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.R
import com.example.buffer.firebase.LikeSongDao
import com.example.buffer.fragmnets.fragment_home
import com.example.buffer.fragmnets.fragment_library
import com.example.buffer.fragmnets.fragment_search
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.service.MusicPlayerService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_music_play.*


class MainActivity : AppCompatActivity() {
    private lateinit var prefrenceService: SharedPrefrenceService
    private lateinit var mMusicPlayerService: MusicPlayerService
    private var mBound = false
    private lateinit var dao: LikeSongDao
    private lateinit var mMessageReceiver:BroadcastReceiver
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("Abhishek", " ServiceConnected")
            val myServiceBinder = p1 as MusicPlayerService.MyServiceBinder
            mMusicPlayerService = myServiceBinder.getService()
            mBound = true

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
            Log.d("Abhishek", " ServiceNotConnected" + p0)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        prefrenceService = SharedPrefrenceService()
        prefrenceService.init(this)

        checkIsPlaying()

           dao = LikeSongDao()
        val homeFragment = fragment_home()
        val searchFragment = fragment_search()
        val settingsFragment = fragment_library()
        setCurrentFragment(homeFragment, "home")


      if(intent.getStringExtra("External")=="yes"){
          val intent = Intent(this,PlayerActivity::class.java)
          startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
      }

        bottom_navigation_bar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Home -> {
                    supportActionBar!!.hide()
                    setCurrentFragment(homeFragment, "Home")
                }
                R.id.search -> setCurrentFragment(searchFragment, "Search")
                R.id.library -> {
                    supportActionBar!!.hide()
                    setCurrentFragment(settingsFragment, "Like")
                }

            }
            true
        }
        frameSongTitle.isSelected = true
        dao.getLikeSongList(FirebaseAuth.getInstance().currentUser!!.uid)
        updateBar()
        songFrameLayout.setOnClickListener {
            val intent = Intent(this,PlayerActivity::class.java)
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        frameSongPlay.setOnClickListener {
            if (mBound) {
                val url = prefrenceService.read("SongUrl", "no")
                mMusicPlayerService.prepare(url!!)
                if (mMusicPlayerService.isPlaying()) {
                    prefrenceService.write("isPlaying", "false")
                    mMusicPlayerService.puause()

                    frameSongPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                } else {
                    prefrenceService.write("isPlaying", "true")
                    val intent = Intent(this, MusicPlayerService::class.java)
                    startService(intent)

                    mMusicPlayerService.play()
                    frameSongPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }
        }
        mMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val message = intent.getStringExtra("Event")
                if(message=="pause"){
                    frameSongPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                else if(message=="play"){
                    frameSongPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("custom-event-name")
        );


    }


    private fun setCurrentFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainframe, fragment)
            commit()
        }

    }

    fun checkIsPlaying() {
        if (prefrenceService.read("isPlaying", "No") == "true") {
            frameSongPlay.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            frameSongPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    fun updateBar() {
        val name = prefrenceService.read("SongName", "Buffer")
        if (name == "Buffer") {
     songFrame.visibility=View.GONE

        } else {
            songFrame.visibility=View.VISIBLE
            frameSongTitle.text = name
            val imgurl = prefrenceService.read("SongImage", "")
            Glide.with(this).load(imgurl).placeholder(R.drawable.splashbuffer).into(frameSongImage)
        }
    }


    override fun onResume() {
        super.onResume()
        updateBar()

        dao.getLikeSongList(FirebaseAuth.getInstance().currentUser!!.uid)
        checkIsPlaying()

    }



    override fun onStart() {
        super.onStart()
        Intent(this,MusicPlayerService::class.java).also { intent->
            bindService(intent,mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy()
        if(mBound){
            unbindService(mConnection)
            mBound=false
        }
    }






}