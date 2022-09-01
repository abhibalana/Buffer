package com.example.buffer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.TopSongs



import com.example.buffer.R

class TopSongAdapter(val context: Context ,val listener:onItemClick): RecyclerView.Adapter<TopSongViewHolder>() {
    var songs = ArrayList<TopSongs>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopSongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.top_song_layout,parent,false)
        val topSongView = TopSongViewHolder(view)
        view.setOnClickListener {
            listener.onMusicItemClick(songs[0].tracks?.items?.get(topSongView.adapterPosition)!!)
        }
        return topSongView
    }

    override fun onBindViewHolder(holder: TopSongViewHolder, position: Int) {
      holder.title.text = songs[0].tracks?.items?.get(position)?.title
        Glide.with(context).load(songs[0].tracks?.items?.get(position)?.artworkUrl).into(holder.image)




    }

    override fun getItemCount(): Int {
        return 6
    }
    fun  setArrayList(song: TopSongs){
        songs.clear()
        songs.add(song)
        notifyDataSetChanged()
    }


}
class TopSongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val image = itemView.findViewById<ImageView>(R.id.songImage)
    val title = itemView.findViewById<TextView>(R.id.songName)
}
