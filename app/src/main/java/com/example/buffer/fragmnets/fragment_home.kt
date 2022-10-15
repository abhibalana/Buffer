package com.example.buffer.fragmnets

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.LikeModelClass
import com.example.buffer.Models.TopSongs

import com.example.buffer.R
import com.example.buffer.Repository.AllSongCategoryRep
import com.example.buffer.ViewModels.MainViewModel
import com.example.buffer.ViewModels.MyViewModelFactory
import com.example.buffer.adapters.SongsDashboardAdapter

import com.example.buffer.adapters.TopSongAdapter
import com.example.buffer.adapters.onItemClick
import com.example.buffer.firebase.LikeSongDao
import com.example.buffer.helper.DateTime
import com.example.buffer.helper.constant
import com.example.buffer.service.RetrofitService
import com.example.buffer.ui.HistoryActivity
import com.example.buffer.ui.SettingsActivity
import com.example.buffer.ui.SongDetailsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.fragment_home2.*

import kotlinx.android.synthetic.main.fragment_home2.view.*
import java.util.*
import kotlin.collections.ArrayList


class fragment_home : Fragment(), onItemClick {

     private lateinit var dateTime: DateTime
    lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var songAdapter: TopSongAdapter
    private lateinit var songDashboardAdapter: SongsDashboardAdapter
    private lateinit var songDashboardAdapter1: SongsDashboardAdapter
    private lateinit var songDashboardAdapter2: SongsDashboardAdapter
    private lateinit var songDashboardAdapter3: SongsDashboardAdapter
    private lateinit var dao: LikeSongDao
    var list1 = ArrayList<ItemsItem>()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("FragmentLiveDataObserve")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_home2, container, false)

        dateTime = DateTime()



        view.Wish.text = dateTime.getTimeWish()
        songAdapter = TopSongAdapter(view.context,this)
        songDashboardAdapter = SongsDashboardAdapter(view.context,this)
        songDashboardAdapter1 = SongsDashboardAdapter(view.context,this)
        songDashboardAdapter2 = SongsDashboardAdapter(view.context,this)
        songDashboardAdapter3 = SongsDashboardAdapter(view.context,this)
        dao = LikeSongDao()
        likeSongList()
        view.settings.setOnClickListener{
            startActivity(Intent(activity, SettingsActivity::class.java))
        }
        view.history.setOnClickListener {
            startActivity(Intent(activity,HistoryActivity::class.java))
        }

        view.topsongrecyclerview.layoutManager = GridLayoutManager(activity,2)
        view.top50recyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        view.poprecyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        view.workoutrecyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        view.booksrecyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        viewModel = ViewModelProvider(this, MyViewModelFactory(AllSongCategoryRep(retrofitService))).get(MainViewModel::class.java)
            viewModel.topSongs.observe(this , Observer {
              songAdapter.setArrayList(it)
                view.topsongrecyclerview.adapter=songAdapter

        viewModel.topSongs2.observe(this, Observer {
           // songDashboardAdapter.setArrayList(it)
            view.top50recyclerview.adapter=songDashboardAdapter

            viewModel.topSongs3.observe(this, Observer {
              //  songDashboardAdapter1.setArrayList(it)
                view.poprecyclerview.adapter=songDashboardAdapter1

                viewModel.topSongs4.observe(this, Observer {
                //    songDashboardAdapter2.setArrayList(it)
                    view.workoutrecyclerview.adapter=songDashboardAdapter2




                    viewModel.topSongs5.observe(this, Observer {
                  //      songDashboardAdapter3.setArrayList(it)
                        view.booksrecyclerview.adapter=songDashboardAdapter3


                    })
                   // viewModel.getAllSongCategory(constant.playlist5,"5")

                })
                //viewModel.getAllSongCategory(constant.playlist4,"4")

            })

            //viewModel.getAllSongCategory(constant.playlist3,"3")


        })
       //viewModel.getAllSongCategory(constant.playlist2,"2")
            })
        viewModel.errorMessage.observe(this, Observer {
            Log.d("Abhishek2" , ""+it)
        })
     //  viewModel.getAllSongCategory(constant.playlist1,"1")

        return view
    }


    override fun onStart() {
        super.onStart()
        likeSongList()


    }
    fun likeSongList(){

        dao.getLikeSongList(FirebaseAuth.getInstance().currentUser!!.uid)
         Log.d("Abhishek",""+constant.likeSong)



    }

    override fun onResume() {
        super.onResume()
        likeSongList()
    }

    override fun onMusicItemClick(item: ItemsItem,topSongs: TopSongs) {
        val intent = Intent(activity,SongDetailsActivity::class.java)
        intent.putExtra("SongsData",item)
        intent.putExtra("list",constant.likeSong)
        intent.putExtra("topSong",topSongs)
        startActivity(intent)
    }




}
