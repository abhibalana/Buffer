package com.example.buffer.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.LikeModelClass
import com.example.buffer.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class LikeSongAdapter(val listener:OnClickLikeSong):RecyclerView.Adapter<LikeSongViewHolder>(){
    var lsongs = ArrayList<ItemsItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeSongViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.like_songs,parent,false)
        val viewHolder = LikeSongViewHolder(view)
        view.setOnClickListener {
            listener.onLikeSongClicked(lsongs.get(viewHolder.adapterPosition))
        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: LikeSongViewHolder, position: Int) {
        holder.title.text = lsongs.get(position).title
        Glide.with(holder.image.context).load(lsongs.get(position).artworkUrl).into(holder.image)
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
class LikeSongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val image = itemView.findViewById<ImageView>(R.id.songImage)
    val title = itemView.findViewById<TextView>(R.id.songName)
}
interface OnClickLikeSong{
   fun  onLikeSongClicked(item:ItemsItem)
}