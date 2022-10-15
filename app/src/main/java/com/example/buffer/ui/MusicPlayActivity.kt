package com.example.buffer.ui


import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.R
import com.example.buffer.Repository.AllSongCategoryRep
import com.example.buffer.ViewModels.MainViewModel
import com.example.buffer.ViewModels.MyViewModelFactory
import com.example.buffer.firebase.LikeSongDao
import com.example.buffer.helper.SharedPrefrenceService
import com.example.buffer.helper.constant
import com.example.buffer.service.MusicPlayerService
import com.example.buffer.service.RetrofitService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_music_play.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MusicPlayActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private lateinit var auth : FirebaseAuth
    private lateinit var dao: LikeSongDao
    var likelist = ArrayList<ItemsItem>()
     var song:ItemsItem = ItemsItem()
    private lateinit var mMessageReceiver:BroadcastReceiver

    private val retrofitService = RetrofitService.getInstance()
    private lateinit var url:String
    private lateinit var mMusicPlayerService: MusicPlayerService
    private var mBound=false
    var like = false
    private lateinit var pref:SharedPrefrenceService
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
        setContentView(R.layout.activity_music_play)
        supportActionBar!!.hide()
        pref = SharedPrefrenceService()
        pref.init(this)
        auth = FirebaseAuth.getInstance()
        dao= LikeSongDao()
        val list = pref.getArrayList("RecentList","No")
         song = intent.getParcelableExtra<ItemsItem>("song")!!
        if(!list.contains(song)){
            list.add(song)
            pref.writeArrayList("RecentList",list)
        }
        val listtype = intent.getStringExtra("listType")
        if(listtype =="likeList"){
            val list =intent.getParcelableArrayListExtra<ItemsItem>("liked")!!
            likelist.addAll(list)
        }
        intializeSong()

        likeSongButton.setOnClickListener {
            val user = auth.currentUser!!
            dao.updateSongList(song, user.uid)
            if(like){
                Glide.with(this).load(R.drawable.ic_baseline_favorite_border_24).into(likeSongButton)
                like=false
            }
            else{
                Glide.with(this).load(R.drawable.ic_baseline_favorite_24).into(likeSongButton)
                like=true
            }
        }
        back.setOnClickListener {
            finish()
        }

        playnext.setOnClickListener {
            if(listtype=="likeList"){
                val rnd = (0 until likelist.size).random()
                song = likelist[rnd]
                if(!list.contains(song)){
                    list.add(song)
                    pref.writeArrayList("RecentList",list)
                }
                intializeSong()

            }
        }
        previous.setOnClickListener {
      mMusicPlayerService.restart()
        }






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
            playSong()


        }
        mMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val message = intent.getStringExtra("Event")
                if(message=="pause"){
                    playMusic.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                else if(message=="play"){
                    playMusic.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("custom-event-name")
        );
    }


    private fun IntializeSeekBar() {
        this.runOnUiThread(object : Runnable {
            override fun run() {
                seekbar.max = mMusicPlayerService.getDuration()
             seekbar.progress=mMusicPlayerService.getCurrentPosition()
                val getCurrent = mMusicPlayerService.getCurrentPosition()
                currentTime.text = String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(getCurrent.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(getCurrent.toLong()) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(getCurrent.toLong())))
                Handler().postDelayed(this, 1000)
            }
        })

    }
    fun intializeSong(){
        Glide.with(this).load(song.artworkUrl).into(PlayingSongImage)
        playing_song_name.text = song.title
        playing_song_name.isSelected=true

        for(i in constant.likeSong){
            if(i.id== song.id){
                Glide.with(this).load(R.drawable.ic_baseline_favorite_24).into(likeSongButton)
                like=true
                break;
            }
        }
        playing_song_name.isSelected = true
        totaltime.text = song.durationText
        playing_song_artist.text = song.publisher?.artist
        viewModel =
            ViewModelProvider(this, MyViewModelFactory(AllSongCategoryRep(retrofitService))).get(
                MainViewModel::class.java
            )
        viewModel.trackData.observe(this, Observer {
            mMusicPlayerService.stop()
            url = it.audio?.get(0)?.url!!
            pref.write("SongName", song.title.toString())
            pref.write("SongImage", song.artworkUrl.toString())
            pref.writeTrack("Track",song)
            mMusicPlayerService.prepare(it.audio?.get(0)?.url!!)
            pref.write("SongUrl", it.audio[0]?.url!!)


            playSong()


        })
        viewModel.getTrackData(song.id!!)
    }

    override fun onStart() {
        super.onStart()
         Intent(this,MusicPlayerService::class.java).also { intent->
            bindService(intent,mConnection,Context.BIND_AUTO_CREATE)
        }

    }

    fun playSong(){
        if (mBound) {

            if (mMusicPlayerService.isPlaying()) {
                pref.write("isPlaying","false")
                mMusicPlayerService.puause()

                playMusic.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            } else {

                val intent = Intent(this,MusicPlayerService::class.java)
                pref.write("isPlaying","true")
                startService(intent)
                mMusicPlayerService.play()
                IntializeSeekBar()

                playMusic.setImageResource(R.drawable.ic_baseline_pause_24)


            }
        }


    }



    override fun onStop() {
        super.onStop()


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        super.onDestroy()
        if(mBound){
            unbindService(mConnection)
            mBound=false
        }
    }




}