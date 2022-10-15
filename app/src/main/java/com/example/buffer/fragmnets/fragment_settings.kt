package com.example.buffer.fragmnets

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import androidx.annotation.RequiresApi
import com.example.buffer.R
import com.example.buffer.authentication.LoginActivity
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.ui.webviewActivity
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class fragment_settings : PreferenceFragment() {
  private lateinit var pref:SharedPrefrenceService

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



}