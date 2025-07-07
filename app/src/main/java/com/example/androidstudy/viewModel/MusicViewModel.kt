package com.example.androidstudy.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Music
import com.example.domain.repository.MusicRepository
import com.example.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val playerRepository: PlayerRepository,
) : ViewModel() {
    private val _musicList = MutableStateFlow<List<Music>>(emptyList())
    val musicList: StateFlow<List<Music>> = _musicList

<<<<<<< Updated upstream
=======
    val isPlaying = playerRepository.isPlaying

    private val _curPos = MutableStateFlow(0L)
    val curPos: StateFlow<Long> = _curPos

>>>>>>> Stashed changes
    fun getMusicList() {
        // 기본적으로 main 스레드에서 실행
        viewModelScope.launch(Dispatchers.Main) {
            musicRepository.getMusicList().collect { result ->
                _musicList.value = result
                setMusicList(result)
            }
        }
    }
<<<<<<< Updated upstream
=======

    fun setMusicList(musicList: List<Music>) {
        playerRepository.setMusicList(musicList)
    }

    fun playMusic(idx: Int) {
        playerRepository.play(idx)
        getMusicCurPosition()
    }

    fun getMusicCurPosition() {
        viewModelScope.launch {
            playerRepository.getCurrentPosition().collect { pos ->
                _curPos.value = pos
                Log.i("=== cur pos === ", pos.toString())
            }
        }
    }

    fun getNextMusic() {

    }

    fun pauseMusic() {
        playerRepository.pause()
    }

    fun resumeMusic() {
        playerRepository.resume()
    }

    override fun onCleared() {
        super.onCleared()
        println("!!! onCleared called !!!")
        playerRepository.release()
    }
>>>>>>> Stashed changes
}