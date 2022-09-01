package com.example.buffer.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buffer.Repository.AllSongCategoryRep

@Suppress("UNCHECKED_CAST")
class MyViewModelFactory constructor(private val repository: AllSongCategoryRep): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}