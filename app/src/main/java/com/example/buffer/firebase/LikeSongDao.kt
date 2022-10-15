package com.example.buffer.firebase

import android.util.Log
import com.example.buffer.Models.ItemsItem
import com.example.buffer.Models.LikeModelClass
import com.example.buffer.helper.constant
import com.google.android.gms.tasks.OnCompleteListener

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.checkerframework.common.value.qual.EnsuresMinLenIf

class LikeSongDao {
    val likedao = FirebaseFirestore.getInstance()
    val collection = likedao.collection("UserLikeSongs")



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

    fun getLikeSongList(uid: String){
        GlobalScope.launch {
            try {
                val list1 = getListById(uid).await().toObject(LikeModelClass::class.java)!!
                constant.likeSong.clear()
                constant.likeSong.addAll(list1.LikeSongs)
            }catch (e:Exception){
                e.printStackTrace()
            }
            }
        }





    fun getListById(uid: String): Task<DocumentSnapshot> {
        return collection.document(uid).get()
    }

}