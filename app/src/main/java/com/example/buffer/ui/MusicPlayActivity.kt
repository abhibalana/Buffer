package com.example.buffer.ui

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.R
import com.example.buffer.Repository.AllSongCategoryRep
import com.example.buffer.ViewModels.MainViewModel
import com.example.buffer.ViewModels.MyViewModelFactory
import com.example.buffer.service.RetrofitService
import kotlinx.android.synthetic.main.activity_music_play.*

class MusicPlayActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var url:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)
        supportActionBar!!.hide()
        val song = intent.getParcelableExtra<ItemsItem>("song")
        Glide.with(this).load(song?.artworkUrl).into(PlayingSongImage)
        playing_song_name.text = song?.title
        playing_song_artist.text = song?.publisher?.artist
        viewModel = ViewModelProvider(this, MyViewModelFactory(AllSongCategoryRep(retrofitService))).get(MainViewModel::class.java)
        viewModel.trackData.observe(this, Observer {
            url = it.audio?.get(0)?.url!!
            Log.d("Abhishek"," url"+url)
        })
        viewModel.getTrackData(song?.id!!)
        Log.d("Abhishek"," id"+song.id)
        sharesong.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        playMusic.setOnClickListener {
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepare()
                start()
            }

        }
    }
}