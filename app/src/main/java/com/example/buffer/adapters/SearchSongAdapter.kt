package com.example.buffer.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.SearchResponse
import com.example.buffer.R
import com.example.buffer.ui.MusicPlayActivity

class SearchSongAdapter(val listener:OnSearchSongClicked):RecyclerView.Adapter<SearchViewHolder>() {
    var searchList = ArrayList<SearchResponse>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.search_song_layout,parent,false)
        val viewHolder = SearchViewHolder(view)
        view.setOnClickListener {
            listener.onSongPlayed(searchList[0].tracks?.items?.get(viewHolder.adapterPosition )!!)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.title.text = searchList[0].tracks?.items?.get(position)?.title
        Glide.with(holder.itemView.context).load(searchList[0].tracks?.items?.get(position)?.artworkUrl).placeholder(R.drawable.splashbuffer).into(holder.image)

    }

    override fun getItemCount(): Int {
        return searchList[0].tracks?.items!!.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateArrayList(response: SearchResponse){
        searchList.clear()
        searchList.add(response)
        notifyDataSetChanged()
    }
}
class SearchViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val image = itemView.findViewById<ImageView>(R.id.songImage)
    val title = itemView.findViewById<TextView>(R.id.songName)

}
interface OnSearchSongClicked{
    fun onSongPlayed(item:ItemsItem)
}