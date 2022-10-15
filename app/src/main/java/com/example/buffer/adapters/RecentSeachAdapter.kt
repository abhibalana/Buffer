package com.example.buffer.adapters

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.R
import com.example.buffer.helper.SharedPrefrenceService
import com.google.android.exoplayer2.Player

class RecentSeachAdapter(val listener:RecentSearchInterface):RecyclerView.Adapter<RecentSearchViewHolder>() {
    var lsongs = ArrayList<ItemsItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {

        val view =  LayoutInflater.from(parent.context).inflate(R.layout.recent_search_song,parent,false)
        val viewHolder = RecentSearchViewHolder(view)
        view.setOnClickListener {
            listener.onSongClicked(lsongs.get(viewHolder.adapterPosition))
        }
        viewHolder.close.setOnClickListener {
            listener.onDeleteSearch(lsongs.get(viewHolder.adapterPosition))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.title.text = lsongs.get(position).title
        Glide.with(holder.image.context).load(lsongs.get(position).artworkUrl).placeholder(R.drawable.splashbuffer).into(holder.image)

    }

    override fun getItemCount(): Int {
       return lsongs.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateArray(songs:ArrayList<ItemsItem>){
        lsongs.clear()
        lsongs.addAll(songs)
        notifyDataSetChanged()
    }
}
class RecentSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val image = itemView.findViewById<ImageView>(R.id.songImage)
    val title = itemView.findViewById<TextView>(R.id.songName)
    val close = itemView.findViewById<ImageView>(R.id.closeSong)
}
interface RecentSearchInterface{
    fun onDeleteSearch(item:ItemsItem)
    fun onSongClicked(item:ItemsItem)
}