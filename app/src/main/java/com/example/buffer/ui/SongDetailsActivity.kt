package com.example.buffer.ui

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.TopSongs

import com.example.buffer.R
import com.example.buffer.Repository.AllSongCategoryRep
import com.example.buffer.ViewModels.MainViewModel
import com.example.buffer.ViewModels.MyViewModelFactory
import com.example.buffer.adapters.SubSongAdapter
import com.example.buffer.adapters.subSongInterface
import com.example.buffer.firebase.LikeSongDao
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.service.MusicPlayerService
import com.example.buffer.service.RetrofitService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.activity_song_details.*

class SongDetailsActivity : AppCompatActivity(), subSongInterface {
    private lateinit var auth :FirebaseAuth
    private lateinit var dao:LikeSongDao
    private lateinit var mMusicPlayerService: MusicPlayerService
    private var mBound=false
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var pref: SharedPrefrenceService
    private lateinit var subSongAdapter: SubSongAdapter
    lateinit var viewModel: MainViewModel
    var like = false
    var isPlaying=false

    var url=""
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("Abhishek"," ServiceConnected")
            val myServiceBinder = p1 as MusicPlayerService.MyServiceBinder
            mMusicPlayerService = myServiceBinder.getService()
            mBound=true

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound=false
            Log.d("Abhishek"," ServiceNotConnected"+p0)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_song_details)
           supportActionBar!!.hide()
        auth = FirebaseAuth.getInstance()
        dao= LikeSongDao()
        pref = SharedPrefrenceService()
        pref.init(this)
        subSongAdapter= SubSongAdapter(this)


        Glide.with(this).load(R.drawable.ic_baseline_favorite_border_24).into(likebutton)
        val item = intent.getParcelableExtra<ItemsItem>("SongsData")!!
        val list = intent.getParcelableArrayListExtra<ItemsItem>("list")!!
        val topSong = intent.getParcelableExtra<TopSongs>("topSong")!!
        for(i in list){
            if(item.id== i.id){
                Glide.with(this).load(R.drawable.ic_baseline_favorite_24).into(likebutton)
                like=true
                break;
            }
        }
        subSongAdapter.updateArrayList(topSong)
        allSongs.layoutManager = LinearLayoutManager(this)
        allSongs.adapter=subSongAdapter

        Glide.with(this).load(item.artworkUrl).into(songDetailsImage)
        songDetailTitle.text = item.title
        Glide.with(this).load(item.artworkUrl).circleCrop().into(userImage)
        userName.text = item.publisher?.artist
        likecount.text = item.likeCount.toString()
        time.text = "1 song: "+item.durationText+"m"+"\n \n\nSongs you may also like\n"
        backButton.setOnClickListener {
            finish()
        }

        likebutton.setOnClickListener{

                val user = auth.currentUser!!
           dao.updateSongList(item, user.uid)
            if(like){
                Glide.with(this).load(R.drawable.ic_baseline_favorite_border_24).into(likebutton)
                like=false
            }
            else{
                Glide.with(this).load(R.drawable.ic_baseline_favorite_24).into(likebutton)
                like=true
            }



            }
        getSongUrl(item)
        playbutton.setOnClickListener {
          playSong()

        }
        }

    fun getSongUrl(item:ItemsItem){
        viewModel =
            ViewModelProvider(this, MyViewModelFactory(AllSongCategoryRep(retrofitService))).get(
                MainViewModel::class.java
            )
        viewModel.trackData.observe(this, Observer {
            mMusicPlayerService.stop()
            url = it.audio?.get(0)?.url!!
            pref.write("SongName", item.title.toString())
            pref.write("SongImage", item.artworkUrl.toString())
            pref.writeTrack("Track",item)
            val list = pref.getArrayList("RecentList","No")
            if(!list.contains(item))list.add(item)
            pref.writeArrayList("RecentList",list)
            mMusicPlayerService.prepare(it.audio?.get(0)?.url!!)
            pref.write("SongUrl", it.audio[0]?.url!!)

            playSong()


        })
        viewModel.getTrackData(item.id!!)
    }

    override fun onStart() {
        super.onStart()
        Intent(this,MusicPlayerService::class.java).also { intent->
            bindService(intent,mConnection, Context.BIND_AUTO_CREATE)
        }


    }

    fun playSong(){
        if (mBound) {

            if (mMusicPlayerService.isPlaying()) {
                mMusicPlayerService.puause()
                pref.write("isPlaying","false")
                playbutton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            } else {
                val intent = Intent(this,MusicPlayerService::class.java)
                startService(intent)
                mMusicPlayerService.play()
                playbutton.setImageResource(R.drawable.ic_baseline_pause_24)
                pref.write("isPlaying","true")

            }
        }


    }

    override fun onStop() {
        super.onStop()
        if(mBound){
            unbindService(mConnection)
            mBound=false
        }
    }

    override fun onSubSongSelected(item: ItemsItem) {
        getSongUrl(item)
    }


}