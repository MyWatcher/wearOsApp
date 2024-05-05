package com.eipsaferoad.owl.utils

import android.content.Context
import android.media.MediaPlayer

fun soundPlayer(context: Context, volume: Float = 1.0f, loop: Boolean = false, fileId: Int) {
    val mediaPlayer = MediaPlayer.create(context, fileId).apply {
        isLooping = loop
        setOnCompletionListener {
            release()
        }
    }
    mediaPlayer.setVolume(volume, volume)
    mediaPlayer.start()
}
