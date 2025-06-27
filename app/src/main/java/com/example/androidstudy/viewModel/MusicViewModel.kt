package com.example.androidstudy.viewModel

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

    val isPlaying = playerRepository.isPlaying

    fun getMusicList() {
        // 기본적으로 main 스레드에서 실행
        viewModelScope.launch(Dispatchers.Main) {
            musicRepository.getMusicList().collect { result ->
                _musicList.value = result
            }
        }
    }

    fun playMusic(uri: String) {
        playerRepository.play(uri)
    }

    override fun onCleared() {
        super.onCleared()
        playerRepository.release()
    }
}