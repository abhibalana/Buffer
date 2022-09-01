package com.example.buffer.firebase

import android.util.Log
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.LikeModelClass
import com.google.android.gms.tasks.OnCompleteListener

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LikeSongDao {
    val likedao = FirebaseFirestore.getInstance()
    val collection = likedao.collection("UserLikeSongs")
    var list = ArrayList<ItemsItem>()
    fun createSongList(likeModelClass: LikeModelClass){
        likeModelClass?.let {
            GlobalScope.launch(Dispatchers.IO) {

                var docref = collection.document(likeModelClass.uid)
                if(!docref.get().await().exists()){
                    collection.document(likeModelClass.uid).set(it)
                }

            }
        }
    }

    fun updateSongList(items: ItemsItem,uid:String){

        GlobalScope.launch {
            val list =  getListById(uid).await().toObject(LikeModelClass::class.java)!!
            if (list.LikeSongs.contains(items)) {
                list.LikeSongs.remove(items)


            } else {
                list.LikeSongs.add(items)


            }
            collection.document(uid).set(list)
        }
    }

    fun containsSong(uid:String):ArrayList<ItemsItem>{
       collection.document(uid).get().addOnCompleteListener { it ->
          if(it.isSuccessful){
              val result = it.result
              result.let {
                  val res = result.toObject(LikeModelClass::class.java)
                  list.clear()
                  list.addAll(res!!.LikeSongs)
              }
          }
      }
return list
    }

    fun getListById(uid: String): Task<DocumentSnapshot> {
        return collection.document(uid).get()
    }

}