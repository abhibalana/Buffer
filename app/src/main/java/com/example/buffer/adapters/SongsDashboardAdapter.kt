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

class SongsDashboardAdapter(val context: Context ,val listener:onItemClick): RecyclerView.Adapter<SongViewHolder>() {
    var songs = ArrayList<TopSongs>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
       val view =  LayoutInflater.from(parent.context).inflate(R.layout.song_layout,parent,false)
       val viewHolder = SongViewHolder(view)
        view.setOnClickListener {
            listener.onMusicItemClick(songs[0].tracks?.items?.get(viewHolder.adapterPosition)!!)
        }
     return viewHolder
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.title.text = songs[0].tracks?.items?.get(position)?.title
        Glide.with(context).load(songs[0].tracks?.items?.get(position)?.artworkUrl).placeholder(R.drawable.splashbuffer).into(holder.image)




    }

    override fun getItemCount(): Int {
        return 10
    }
    fun  setArrayList(song: TopSongs){
        songs.clear()
        songs.add(song)

        notifyDataSetChanged()
    }


}
class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val image = itemView.findViewById<ImageView>(R.id.newSongImage)
    val title = itemView.findViewById<TextView>(R.id.newSongTitle)
}
interface onItemClick{
    fun onMusicItemClick(item:ItemsItem)
}