package com.example.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface PlayerRepository {
    val isPlaying: StateFlow<Boolean>

    fun play(path: String)
    fun release()
}