package com.example.buffer.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPrefrenceService {
    private lateinit var preferences: SharedPreferences
    fun init(context: Context) {
        preferences = context.getSharedPreferences("Login", 0)
    }
    fun read(key:String,Default:String):String?{
       return preferences.getString(key,Default)
    }

    @SuppressLint("CommitPrefEdits")
    fun write(key:String, value:String){
        val editor = preferences.edit()
        editor.putString(key,value)
        editor.apply()

    }
}