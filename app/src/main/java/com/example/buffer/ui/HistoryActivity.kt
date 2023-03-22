package com.example.buffer.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buffer.Models.ItemsItem
import com.example.buffer.R
import com.example.buffer.adapters.LikeSongAdapter
import com.example.buffer.adapters.OnClickLikeSong
import com.example.buffer.adapters.SubSongAdapter
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.helper.constant
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity(), OnClickLikeSong {
    private lateinit var likeSongAdapter: LikeSongAdapter
    private lateinit var pref :SharedPrefrenceService
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar!!.title = "Recent Played"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_color))
        supportActionBar!!.elevation=0f
        likeSongAdapter= LikeSongAdapter(this)
        pref = SharedPrefrenceService()
        pref.init(this)
        val list = pref.getArrayList("RecentList","No")
        likeSongAdapter.updateArray(list,"noLike")
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
          if(!historyRecyclerView.canScrollVertically(1)){
              clearAll.visibility=View.VISIBLE
          }
          if(RecyclerView.SCROLL_STATE_DRAGGING==i){
              clearAll.visibility=View.GONE
          }
        }
        clearAll.setOnClickListener {
            list.clear()
            pref.writeArrayList("RecentList",list)
            likeSongAdapter.updateArray(list,"noLike")
        }
        historyRecyclerView.adapter=likeSongAdapter

    }

    override fun onLikeSongClicked(item: ItemsItem) {
        val intent = Intent(this, MusicPlayActivity::class.java)
        intent.putExtra("song",item)
        intent.putParcelableArrayListExtra("liked",constant.likeSong)
        intent.putExtra("listType","likeList")
        startActivity(intent)
    }

    override fun onHeartClicked(item: ItemsItem) {
        TODO("Not yet implemented")
    }
}