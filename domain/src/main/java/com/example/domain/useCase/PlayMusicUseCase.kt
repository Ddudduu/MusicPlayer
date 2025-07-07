package com.example.domain.useCase

import com.example.domain.repository.PlayerRepository
import javax.inject.Inject

class PlayMusicUseCase @Inject constructor(private val playerRepository: PlayerRepository) {
    operator fun invoke(idx: Int) {
        playerRepository.play(idx)
    }
}