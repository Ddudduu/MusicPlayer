package com.example.data.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.domain.entity.Music
import com.example.domain.repository.MusicRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    MusicRepository {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun getMusicList(): Flow<List<Music>> = flow {
        val musicList = mutableListOf<Music>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        // 가져올 정보
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Downloads.SIZE,
            MediaStore.Audio.Media.ALBUM_ID
        )

        // 쿼리문
        val selection = "${MediaStore.Audio.Media.MIME_TYPE} LIKE ?"
        // 위에서 선언한 쿼리문의 ? 에 들어갈 값
        val selectionArgs = arrayOf("audio/mpeg")
        // 쿼리 정렬
        val selectionSort = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
        val cursor = context.contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            selectionSort
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
//            val typeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
//            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            println("Cursor count : ${cursor.count}")

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn).substringBeforeLast(".")
                val artist = cursor.getString(artistColumn)
                val audioUri = ContentUris.withAppendedId(uri, id)

                Log.i("🎶🎧mp3 list :", "$id, $name, $artist, $audioUri")

                val music = Music(id, name, artist, audioUri.toString())
                musicList.add(music)

                // album uri 유효한지 확인
//                val exist = try {
//                    context.contentResolver.openInputStream(albumUri)?.close()
//                    true
//                } catch (e: Exception) {
//                    false
//                }
//
//                Log.i("=== Does AlbumArt $albumUri exist? ", "$exist")
            }
        }

        emit(musicList)
        //  IO 스레드 실행하도록 명시
    }.flowOn(Dispatchers.IO)
}