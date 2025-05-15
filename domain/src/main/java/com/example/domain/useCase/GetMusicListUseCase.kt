package com.example.domain.useCase

import com.example.domain.entity.Music
import com.example.domain.repository.MusicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMusicListUseCase @Inject constructor(private val repository: MusicRepository) {
    operator fun invoke(): Flow<List<Music>> = repository.getMusicList()
}