package com.example.data.repository

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.example.data.di.ExoPlayerProvider
import com.example.domain.entity.Music
import com.example.domain.entity.PlayerError
import com.example.domain.entity.PlayerErrorType
import com.example.domain.repository.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val exoPlayerProvider: ExoPlayerProvider,
) :
    PlayerRepository {
    private var exoPlayer = exoPlayerProvider.createPlayer()
    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isMediaItemSet = MutableStateFlow(false)
    override val isMediaItemSet: StateFlow<Boolean> = _isMediaItemSet.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    override val duration: StateFlow<Long> = _duration

    private val _curTitle = MutableStateFlow("")
    override val curTitle: StateFlow<String> = _curTitle.asStateFlow()

    private val _curUri = MutableStateFlow("")
    override val curUri: StateFlow<String> = _curUri.asStateFlow()

    private val _playerError = MutableSharedFlow<PlayerError>()
    override val playerError: SharedFlow<PlayerError> = _playerError

    private var onSeekFinishedCallback: (() -> Unit)? = null

    private var prevMusicList: List<Music> = emptyList()

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

            // get album cover uri
            mediaItem?.localConfiguration?.uri.let {
                Log.w("local uri ", it.toString())
                _curUri.value = it.toString()
            }
            // update duration when music changed
            _duration.value = exoPlayer.duration
        }

        override fun onPlayerError(error: PlaybackException) {
            // clear current music info
            _curTitle.value = ""
            _curUri.value = ""
            val errorType = when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> PlayerErrorType.SOURCE_ERROR
                PlaybackException.ERROR_CODE_DECODING_FAILED -> PlayerErrorType.DECODING_ERROR
                PlaybackException.ERROR_CODE_PARSING_CONTAINER_UNSUPPORTED -> PlayerErrorType.UNSUPPORTED_FORMAT
                else -> PlayerErrorType.UNKNOWN_ERROR
            }

            // emit error event
            CoroutineScope(Dispatchers.IO).launch {
                _playerError.emit(PlayerError(type = errorType, message = error.localizedMessage))
            }
            recreatePlayer()
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

        prevMusicList = musicList
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        _isMediaItemSet.value = true
    }

    override fun play(idx: Int) {
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

    override fun playNextMusic() {
        if (exoPlayer.hasNextMediaItem()) {
            play(exoPlayer.nextMediaItemIndex)
        } else {
            play(0)
        }
    }

    override fun playPrevMusic() {
        if (exoPlayer.hasPreviousMediaItem()) {
            play(exoPlayer.previousMediaItemIndex)
        } else {
            play(exoPlayer.mediaItemCount - 1)
        }
    }

    private fun recreatePlayer() {
        exoPlayer.release()
        exoPlayer = exoPlayerProvider.createPlayer()
        _isMediaItemSet.value = false
        prevMusicList?.let { setMusicList(it) }
        exoPlayer.addListener(exoPlayerListener)
    }
}
