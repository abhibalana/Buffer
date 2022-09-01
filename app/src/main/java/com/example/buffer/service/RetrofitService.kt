package com.example.buffer.service

import com.example.buffer.Models.SearchResponse
import com.example.buffer.Models.TopSongs
import com.example.buffer.Models.TrackResponse

import com.example.buffer.helper.constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("playlist/tracks")
    fun getAllSongCategories(@Header("X-RapidAPI-Host") host:String,@Header("X-RapidAPI-Key") key:String ,@Query("playlist") url:String ,@Query("limit") limit:Int) : Call<TopSongs>

    @GET("search/tracks")
    fun getSearchSongList(@Header("X-RapidAPI-Host") host:String,@Header("X-RapidAPI-Key") key:String ,@Query("term") url:String ,@Query("limit") limit:Int):Call<SearchResponse>

    @GET("track/metadata")
    fun getSongMetaData(@Header("X-RapidAPI-Host") host:String,@Header("X-RapidAPI-Key") key:String ,@Query("track") id:Int) : Call<TrackResponse>

    companion object {
        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(constant.Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}