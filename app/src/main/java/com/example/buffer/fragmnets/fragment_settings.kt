package com.example.buffer.fragmnets

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.buffer.R
import com.example.buffer.authentication.LoginActivity
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.service.MusicPlayerService
import com.example.buffer.ui.webviewActivity
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class fragment_settings : PreferenceFragment() {
  private lateinit var pref:SharedPrefrenceService
    private lateinit var mMusicPlayerService: MusicPlayerService
    private var mBound=false
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("Abhishek"," ServiceConnectedin playerActivity")
            val myServiceBinder = p1 as MusicPlayerService.MyServiceBinder
            mMusicPlayerService = myServiceBinder.getService()
            mBound=true

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound=false
            Log.d("Abhishek"," ServiceNotConnected"+p0)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        pref = SharedPrefrenceService()
        pref.init(activity)



    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPreferenceTreeClick(
        preferenceScreen: PreferenceScreen?,
        preference: Preference?
    ): Boolean {
        val key = preference!!.key
        when(key){
            resources.getString(R.string.logout_key) -> {
                FirebaseAuth.getInstance().signOut()
                AsyncTask.execute{
                    try {
                        mMusicPlayerService.stop1()
                    }catch (e:Exception){
                        e.stackTrace
                    }
                }

                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                pref.write("isPlaying","false")

                startActivity(intent)
                activity.finish()

            }
            resources.getString(R.string.terms_key)->{
                val intent = Intent(activity,webviewActivity::class.java)
                intent.putExtra("Privacy","Terms&Conditions")
                startActivity(intent)
            }
            resources.getString(R.string.privacy_key)->{
                val intent = Intent(activity,webviewActivity::class.java)
                intent.putExtra("Privacy","Privacy Policy")
                startActivity(intent)
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(activity,MusicPlayerService::class.java)
        activity.bindService(intent,mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        if(mBound){
            activity.unbindService(mConnection)
            mBound=false
        }
        super.onDestroy()
    }



}