package com.example.domain.useCase

import com.example.domain.repository.MusicRepository
import jakarta.inject.Inject

class GetMusicListUseCase @Inject constructor(private val repository: MusicRepository) {
    suspend operator fun invoke() = repository.getMusicList()
}