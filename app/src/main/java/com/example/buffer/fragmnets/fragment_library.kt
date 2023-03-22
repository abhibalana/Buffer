package com.example.buffer.fragmnets

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyCallback
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.LikeModelClass
import com.example.buffer.R
import com.example.buffer.adapters.LikeSongAdapter
import com.example.buffer.adapters.OnClickLikeSong
import com.example.buffer.firebase.LikeSongDao
import com.example.buffer.ui.MainActivity
import com.example.buffer.ui.MusicPlayActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_library.view.*


class fragment_library : Fragment(), OnClickLikeSong {
private lateinit var adapter: LikeSongAdapter
    private lateinit var dao: LikeSongDao
    private lateinit var auth : FirebaseAuth
    var likes :ArrayList<ItemsItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        dao= LikeSongDao()
        val name = FirebaseAuth.getInstance().currentUser!!.displayName
        val s = name!!.split(" ")


        val view =  inflater.inflate(R.layout.fragment_library, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        view.LikeSongs.text = s[0]+","
        view.LikeSongsrecycler.layoutManager= LinearLayoutManager(activity)
        adapter= LikeSongAdapter(this)


        setUpRecylerView()


    return view;
    }

    private fun setUpRecylerView() {
        Log.d("Abhishek","function called")
        val dao = LikeSongDao()
        val song = dao.collection
       song.document(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnCompleteListener {
           if(it.isSuccessful){
               val likesongs = it.getResult().toObject(LikeModelClass::class.java)!!
               if(likesongs.LikeSongs.size!=0){
                   Log.d("abhishek","entered")
                   likes.clear()
                   likes.addAll(likesongs.LikeSongs)
                   view?.nolike?.visibility=View.GONE
                   view?.noliketext?.visibility=View.GONE
                   adapter.updateArray(likesongs.LikeSongs,"likeSongs")
                   view?.LikeSongsrecycler?.adapter=adapter
               }
           }
       }

    }

    override fun onLikeSongClicked(item: ItemsItem) {
        Log.d("ABHISHEK","SONG CLICKED")
        val intent = Intent(activity, MusicPlayActivity::class.java)
        intent.putExtra("song",item)
        intent.putParcelableArrayListExtra("liked",likes)
        intent.putExtra("listType","likeList")
        startActivity(intent)
    }

    override fun onHeartClicked(item: ItemsItem) {
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle(item.title).setMessage("want to delete?")
            .setPositiveButton("Yes"){ dialog,whichButton->
                val user = auth.currentUser!!
                dao.updateSongList(item, user.uid)
                likes.remove(item)
                adapter.updateArray(likes,"likeSongs")

            }.setNegativeButton("No"){dialog,whichbutton->

            }.show()
    }

    override fun onResume() {
        super.onResume()
        setUpRecylerView()
    }



}