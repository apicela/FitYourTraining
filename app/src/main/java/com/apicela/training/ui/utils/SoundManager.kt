package com.apicela.training.ui.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.apicela.training.R

class SoundManager(context: Context, private var x: Int) {
    private lateinit var mediaPlayer: MediaPlayer

    init {
        mediaPlayer = MediaPlayer.create(context, R.raw.beep)
    }

    fun playSound() {
        Log.d("music", "play sound")
        mediaPlayer.start()
    }

    fun release() {
        mediaPlayer.release()
    }

}