package com.example.buffer.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.TrackResponse
import com.example.buffer.fragmnets.fragment_search
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

@Suppress("UnstableApiUsage")
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
    fun writeTrack(key:String,value:ItemsItem){
        val editor = preferences.edit()
        val gson = Gson()
        val str = gson.toJson(value)
        editor.putString(key,str);
        Log.d("Abhishek"," "+str)
        editor.commit()
    }
    fun getTrack(key:String,default:String):ItemsItem{
        val gson = Gson()
        val str = preferences.getString(key,default)
        val obj = gson.fromJson(str,ItemsItem::class.java)
        return obj
    }
    fun writeArrayList(key:String,value:ArrayList<ItemsItem>){
        val editor = preferences.edit()
        val gson = Gson()
        val str = gson.toJson(value)
        editor.putString(key,str);
        Log.d("Abhishek"," "+str)
        editor.commit()
    }
    fun getArrayList(key:String,default:String):ArrayList<ItemsItem>{
        val gson = Gson()
        val str = preferences.getString(key,default)
        val type = object :TypeToken<ArrayList<ItemsItem>>(){}.type
        val obj = gson.fromJson<Any>(str,type) as ArrayList<ItemsItem>
        return obj
    }






}