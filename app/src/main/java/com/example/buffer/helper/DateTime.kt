package com.example.buffer.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTime() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeWish():String{


        var currentDateTime= LocalDateTime.now()
        var time = currentDateTime.format(DateTimeFormatter.ofPattern("HH"))
        if(time>="0" && time<"12"){
            return "Good morning"
        }
        else if(time>="12"&&time<"18"){
            return "Good afternoon"
        }
        return "Good evening"
    }

}