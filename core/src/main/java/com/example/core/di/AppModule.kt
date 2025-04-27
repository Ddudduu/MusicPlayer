package com.example.core.di

import android.content.Context
import android.util.Printer
import com.example.data.repository.MusicRepositoryImpl
import com.example.domain.repository.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideMusicRepository(@ApplicationContext context: Context): MusicRepository {
        return MusicRepositoryImpl(context)
    }

}