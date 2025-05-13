package com.example.domain.repository

import com.example.domain.entity.Music
import kotlinx.coroutines.flow.Flow


interface MusicRepository {
    fun getMusicList(): Flow<List<Music>>
}