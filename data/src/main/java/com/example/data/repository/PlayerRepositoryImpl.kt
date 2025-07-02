package com.example.data.repository

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.domain.entity.Music
import com.example.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val exoPlayer: ExoPlayer,
) :
    PlayerRepository {
    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> get() = _isPlaying

    // ExoPlayer 재생 상태 값을 받아오기 위한 리스너
    private val exoPlayerListener: Player.Listener = createListener()
    private fun createListener() = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
            println("$$ isPlaying $$ $isPlaying")
        }
    }

    init {
        exoPlayer.addListener(exoPlayerListener)
    }

    override fun setMusicList(musicList: List<Music>) {
        val mediaItems = musicList.map {
            MediaItem.fromUri(Uri.parse(it.musicUri))
        }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()

        Log.i("== exoPlayer media ==", exoPlayer.mediaItemCount.toString())
    }

    override fun play(idx: Int) {
//        if (isPlaying.value) {
//            exoPlayer.stop()
//        }

//        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(path)))
//        exoPlayer.prepare()
        Log.i(" == play called! ==", idx.toString())
        exoPlayer.seekTo(idx, 0)
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    // 일시정지 시점부터 다시 재생
    override fun resume() {
        exoPlayer.play()
    }

    override fun release() {
        exoPlayer.release()
        exoPlayer.removeListener(exoPlayerListener)
    }
}