package com.example.domain.entity

data class PlayerError(
    val type: PlayerErrorType,
    val message: String? = null,
)
