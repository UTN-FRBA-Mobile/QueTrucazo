package com.utnmobile.quetrucazo.music

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MusicModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MusicModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}