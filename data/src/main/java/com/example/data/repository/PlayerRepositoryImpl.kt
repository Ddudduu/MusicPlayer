package com.example.data.repository

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.domain.entity.Music
import com.example.domain.repository.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val exoPlayer: ExoPlayer,
) :
    PlayerRepository {
    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isMediaItemSet = MutableStateFlow(false)
    override val isMediaItemSet: StateFlow<Boolean> = _isMediaItemSet.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    override val duration: StateFlow<Long> = _duration

    private val _curTitle = MutableStateFlow("")
    override val curTitle: StateFlow<String> = _curTitle.asStateFlow()

    private var onSeekFinishedCallback: (() -> Unit)? = null

    // ExoPlayer 재생 상태 값을 받아오기 위한 리스너
    private val exoPlayerListener: Player.Listener = createListener()
    private fun createListener() = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
            println("$$ isPlaying $$ $isPlaying")
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                _duration.value = exoPlayer.duration
            }
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int,
        ) {
            if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                onSeekFinishedCallback?.invoke()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.mediaMetadata?.title.let {
                _curTitle.value = it.toString()
            }
        }
    }

    init {
        exoPlayer.addListener(exoPlayerListener)
    }

    override fun setMusicList(musicList: List<Music>) {
        if (isMediaItemSet.value) return

        // set meta data implicitly
        val mediaItems = musicList.map { music ->
            MediaItem.Builder()
                .setUri(Uri.parse(music.musicUri))
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(music.title)
                        .setArtist(music.artist).build()
                ).build()
        }

        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        _isMediaItemSet.value = true
    }

    override fun play(idx: Int) {
        Log.i(" == play called! ==", idx.toString())

        exoPlayer.seekTo(idx, 0)
        exoPlayer.play()
    }

    override fun seek(pos: Long) {
        exoPlayer.seekTo(pos)
    }

    fun setOnSeekFinishedCallback(callback: (() -> Unit)) {
        onSeekFinishedCallback = callback
    }

    override fun getCurrentPosition(): Flow<Long> = callbackFlow {
        // access main thread
        val updateJob = CoroutineScope(Dispatchers.Main).launch {
            // launch job while coroutine is active
            while (isActive) {
                val isExoPlayerReady =
                    exoPlayer.playbackState == Player.STATE_READY || exoPlayer.playbackState == Player.STATE_BUFFERING

                if (isExoPlayerReady) {
                    trySend(exoPlayer.currentPosition)
                }
                delay(200L)
            }
        }

        setOnSeekFinishedCallback { trySend(exoPlayer.currentPosition) }

        // remove resource
        awaitClose {
            updateJob.cancel()
            onSeekFinishedCallback = null
        }
        // emit values in IO to prevent ANR
    }.flowOn(Dispatchers.IO)

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