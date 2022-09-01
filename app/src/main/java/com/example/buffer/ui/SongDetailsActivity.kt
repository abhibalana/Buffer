package com.example.buffer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem

import com.example.buffer.R
import com.example.buffer.firebase.LikeSongDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_song_details.*

class SongDetailsActivity : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth
    private lateinit var dao:LikeSongDao
    private lateinit var list: ArrayList<ItemsItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_song_details)
           supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
        dao= LikeSongDao()

        list = ArrayList()
        Glide.with(this).load(R.drawable.ic_baseline_favorite_border_24).into(likebutton)
        val item = intent.getParcelableExtra<ItemsItem>("SongsData")!!
        for(i in 0 until list.size) {
            if (list[i].id==item.id) {
                Glide.with(this).load(R.drawable.ic_baseline_favorite_24).into(likebutton)
            }
        }
        Glide.with(this).load(item.artworkUrl).into(songDetailsImage)
        songDetailTitle.text = item.title
        Glide.with(this).load(item.artworkUrl).circleCrop().into(userImage)
        userName.text = item.publisher?.artist
        likecount.text = item.likeCount.toString()
        time.text = "1 song: "+item.durationText+"m"

        likebutton.setOnClickListener{

                val user = auth.currentUser!!
           dao.updateSongList(item, user.uid)

                   Glide.with(this).load(R.drawable.ic_baseline_favorite_24).into(likebutton)


            }

        }

    override fun onStart() {
        super.onStart()
        val dao = LikeSongDao()

        list = dao.containsSong(FirebaseAuth.getInstance().currentUser!!.uid)
    }





}