package com.example.domain.repository

import com.example.domain.entity.Music
import kotlinx.coroutines.flow.StateFlow

interface PlayerRepository {
    val isMediaItemSet: StateFlow<Boolean>
    val isPlaying: StateFlow<Boolean>

    fun setMusicList(musicList: List<Music>)
    fun play(idx: Int)

    fun pause()
    fun resume()
    fun release()
}