package com.example.buffer.fragmnets

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buffer.Models.ItemsItem
import com.example.buffer.R
import com.example.buffer.Repository.AllSongCategoryRep
import com.example.buffer.ViewModels.MainViewModel
import com.example.buffer.ViewModels.MyViewModelFactory
import com.example.buffer.adapters.OnSearchSongClicked
import com.example.buffer.adapters.SearchSongAdapter
import com.example.buffer.service.RetrofitService
import com.example.buffer.ui.MusicPlayActivity
import kotlinx.android.synthetic.main.activity_music_play.*
import kotlinx.android.synthetic.main.fragment_search.view.*


class fragment_search : Fragment(), OnSearchSongClicked {


    lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private lateinit var searchSongAdapter:SearchSongAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_search, container, false)
        searchSongAdapter = SearchSongAdapter(this)
        view.searchSongRecyclerView.layoutManager = LinearLayoutManager(activity)
        setHasOptionsMenu(true)
        view.searchview.setOnClickListener {
            (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.teal_600)))
            (activity as AppCompatActivity).supportActionBar?.title=""
            (activity as AppCompatActivity).supportActionBar?.show()
            view.searchview.visibility=View.GONE
            view.search_text.visibility=View.GONE
        }
        viewModel = ViewModelProvider(this, MyViewModelFactory(AllSongCategoryRep(retrofitService))).get(MainViewModel::class.java)

     return view
    }






    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu,menu)
        val searchView = SearchView((activity as AppCompatActivity).supportActionBar?.themedContext!!)

       menu.findItem(R.id.searchAction).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                view?.searchProgressbar?.visibility = View.VISIBLE
                if(!query.isEmpty()) {
                    Log.d("Abhishek", "querty " + query)
                    viewModel.searchSong.observe(this@fragment_search, Observer {
                        view?.searchProgressbar?.visibility = View.GONE
                        if(it.status!!) {
                            searchSongAdapter.updateArrayList(it)
                            view?.searchSongRecyclerView?.adapter = searchSongAdapter
                        }
                        else{
                            view?.noDataText?.visibility=View.VISIBLE
                        }
                    })
                    viewModel.getSearchSong(query)
                }
                else{
                    view?.searchProgressbar?.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
        searchView.setOnClickListener {view ->  }
    }

    override fun onSongPlayed(item: ItemsItem) {
        val intent = Intent(activity,MusicPlayActivity::class.java)
        intent.putExtra("song",item)
        startActivity(intent)
    }


}