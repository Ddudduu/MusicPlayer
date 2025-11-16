package com.example.androidstudy.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import com.example.androidstudy.utils.formatMillisToMinSec
import com.example.domain.entity.Music
import com.example.domain.repository.MusicRepository
import com.example.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val _curPos = MutableStateFlow(0L)
    val curPos: StateFlow<Long> = _curPos
    val duration = playerRepository.duration

    val curTitle = playerRepository.curTitle
    val curUri = playerRepository.curUri

    private val _showErrorDialog = MutableStateFlow<Boolean>(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    val curPosDurationFormatted: StateFlow<String> = _curPos.combine(duration) { pos, dur ->
        formatMillisToMinSec(pos) to formatMillisToMinSec(dur.takeIf { it != C.TIME_UNSET } ?: 0L)
    }
        .map { (posStr, durStr) -> "$posStr / $durStr" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "0:00 / 0:00")

    init {
        viewModelScope.launch {
            playerRepository.playerError.collect { error ->
                if (error.type != null) {
                    _showErrorDialog.value = true
                }
            }
        }
    }

    fun dismissDialog() {
        _showErrorDialog.value = false
    }

    fun getMusicList() {
        // 기본적으로 main 스레드에서 실행
        viewModelScope.launch(Dispatchers.Main) {
            musicRepository.getMusicList().collect { result ->
                _musicList.value = result
                setMusicList(result)
            }
        }
    }

    fun setMusicList(musicList: List<Music>) {
        playerRepository.setMusicList(musicList)
    }

    fun playMusic(idx: Int) {
        playerRepository.play(idx)
        getMusicCurPosition()
    }

    fun seek(pos: Long) {
        playerRepository.seek(pos)
    }

    fun getMusicCurPosition() {
        viewModelScope.launch {
            playerRepository.getCurrentPosition().collect { pos ->
                _curPos.value = pos
            }
        }
    }

    fun playNextMusic() {
        playerRepository.playNextMusic()
    }

    fun playPrevMusic() {
        playerRepository.playPrevMusic()
    }

    fun pauseMusic() {
        playerRepository.pause()
    }

    fun resumeMusic() {
        playerRepository.resume()
    }

    override fun onCleared() {
        super.onCleared()
        playerRepository.release()
    }
}
