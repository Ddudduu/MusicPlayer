package com.example.domain.repository

interface PlayerRepository {
    fun play(path: String)
    fun pause()
    fun release()
}