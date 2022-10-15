package com.example.buffer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.SearchResponse
import com.example.buffer.Models.TopSongs
import com.example.buffer.R
import org.w3c.dom.Text

class SubSongAdapter(val listener:subSongInterface):RecyclerView.Adapter<SubSongViewHolder>() {
    var searchList = ArrayList<TopSongs>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubSongViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.search_song_layout,parent,false)
        val viewHolder = SubSongViewHolder(view)
        view.setOnClickListener {
            listener.onSubSongSelected(searchList[0].tracks?.items?.get(viewHolder.adapterPosition)!!)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SubSongViewHolder, position: Int) {
        holder.title.text = searchList[0].tracks?.items?.get(position)?.title
        holder.publsiherName.text = searchList[0].tracks?.items?.get(position)?.publisher?.artist
        Glide.with(holder.itemView.context).load(searchList[0].tracks?.items?.get(position)?.artworkUrl).placeholder(R.drawable.splashbuffer).into(holder.image)
    }

    override fun getItemCount(): Int {
        return searchList[0].tracks?.items?.size!!

    }
    fun updateArrayList(response: TopSongs){
        searchList.clear()
        searchList.add(response)
        notifyDataSetChanged()
    }
}
class SubSongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val image = itemView.findViewById<ImageView>(R.id.songImage)
    val title = itemView.findViewById<TextView>(R.id.songName)
    val publsiherName = itemView.findViewById<TextView>(R.id.publisherName)

}
interface subSongInterface{
    fun onSubSongSelected(item:ItemsItem)
}