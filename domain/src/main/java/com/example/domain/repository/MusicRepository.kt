package com.example.domain.repository

import com.example.domain.entity.Music

interface MusicRepository {
    suspend fun getMusicList(): List<Music>
}