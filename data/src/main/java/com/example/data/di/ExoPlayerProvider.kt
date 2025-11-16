package com.example.data.di

import android.content.Context
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun createPlayer(): ExoPlayer {
        Log.i("=== create player!! ===", "")
        return ExoPlayer.Builder(context).build()
    }
}