package com.example.androidstudy.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.repository.MusicRepositoryImpl
import com.example.data.repository.PlayerRepositoryImpl
import com.example.domain.repository.MusicRepository
import com.example.domain.repository.PlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideMusicRepository(@ApplicationContext context: Context): MusicRepository {
        return MusicRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun providePlayerRepository(exoPlayer: ExoPlayer): PlayerRepository {
        return PlayerRepositoryImpl(exoPlayer)
    }
}