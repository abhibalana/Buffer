package com.example.buffer.Repository

import com.example.buffer.helper.constant
import com.example.buffer.service.RetrofitService
import kotlin.contracts.contract

class AllSongCategoryRep constructor(private val retrofitService: RetrofitService) {
    fun getAllSongCategory(playlist:String) = retrofitService.getAllSongCategories(constant.host,constant.api_key,playlist,constant.limit)
    fun getSearchSong(query:String) = retrofitService.getSearchSongList(constant.host,constant.api_key,query,20)

    fun getTrack(id:Int) = retrofitService.getSongMetaData(constant.host,constant.api_key,id)

}