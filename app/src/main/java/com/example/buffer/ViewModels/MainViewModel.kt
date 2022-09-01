package com.example.buffer.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.SearchResponse
import com.example.buffer.Models.TopSongs
import com.example.buffer.Models.TrackResponse
import com.example.buffer.Repository.AllSongCategoryRep
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel constructor(private val repository: AllSongCategoryRep)  : ViewModel() {

    val topSongs = MutableLiveData<TopSongs>()
    val topSongs2 = MutableLiveData<TopSongs>()
    val errorMessage = MutableLiveData<String>()
    val topSongs3 = MutableLiveData<TopSongs>()
    val topSongs4 = MutableLiveData<TopSongs>()
    val topSongs5 = MutableLiveData<TopSongs>()
    val searchSong = MutableLiveData<SearchResponse>()
    val trackData = MutableLiveData<TrackResponse>()



    fun getTrackData(id:Int){
        val response = repository.getTrack(id)
        response.enqueue(object :Callback<TrackResponse>{
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                trackData.postValue(response.body())
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
                Log.d("Abhishek"," message"+t.message)
            }

        })
    }


    fun getSearchSong(query:String){
        val response = repository.getSearchSong(query)
        response.enqueue(object:Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
              searchSong.postValue(response.body())
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
             errorMessage.postValue(t.message)
            }

        })
    }

    fun getAllSongCategory(playlist:String,play:String) {
        val response = repository.getAllSongCategory(playlist)
       response.enqueue(object : Callback<TopSongs>{
           override fun onResponse(call: Call<TopSongs>, response: Response<TopSongs>) {
               if(play=="1") {
                   topSongs.postValue(response.body())
               }
               else if(play=="2"){
                   topSongs2.postValue(response.body())
               }
               else if(play=="3"){
                   topSongs3.postValue(response.body())
               }
               else if(play=="4"){
                   topSongs4.postValue(response.body())
               }
               else if(play=="5"){
                   topSongs5.postValue(response.body())
               }

           }

           override fun onFailure(call: Call<TopSongs>, t: Throwable) {
               errorMessage.postValue(t.message)
           }

       })

    }


}