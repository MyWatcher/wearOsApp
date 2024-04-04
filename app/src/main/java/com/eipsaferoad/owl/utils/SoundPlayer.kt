package com.eipsaferoad.owl.utils

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun SoundPlayer(volume: Float = 1.0f, loop: Boolean = false, fileId: Int) {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, fileId)
            .apply {
                isLooping = loop
            }
    }

    LocalLifecycleOwner.current.lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                mediaPlayer.release()
            }
        }
    )
    mediaPlayer.setVolume(volume, volume)
    mediaPlayer.start()
}
