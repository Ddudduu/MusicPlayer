package com.example.data.repository

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.domain.repository.PlayerRepository
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(private val exoPlayer: ExoPlayer) :
    PlayerRepository {
    override fun play(path: String) {
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(path)))
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun release() {
        exoPlayer.release()
    }
}