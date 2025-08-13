package com.example.domain.repository

import com.example.domain.entity.Music
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlayerRepository {
    val isMediaItemSet: StateFlow<Boolean>
    val isPlaying: StateFlow<Boolean>

    val duration: StateFlow<Long>
    val curTitle: StateFlow<String>
    val curUri: StateFlow<String>

    fun setMusicList(musicList: List<Music>)
    fun play(idx: Int)

    fun seek(pos: Long)

    fun getCurrentPosition(): Flow<Long>
    fun pause()
    fun resume()
    fun release()
}