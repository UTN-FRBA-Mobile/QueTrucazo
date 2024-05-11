package com.utnmobile.quetrucazo.ui.music

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import com.utnmobile.quetrucazo.R

class MusicViewModel(context: Context) : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null

    init {
        mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.fluffingaduck)
        mediaPlayer?.isLooping = true
    }

    fun playMusic() {
        mediaPlayer?.start()
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun setVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
    }

    override fun onCleared() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}