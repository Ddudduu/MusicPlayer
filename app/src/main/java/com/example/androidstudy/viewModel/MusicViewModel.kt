package com.example.androidstudy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Music
import com.example.domain.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(private val repository: MusicRepository) : ViewModel() {
    private val _musicList = MutableLiveData<List<Music>>()
    val musicList: LiveData<List<Music>> = _musicList

    fun getMusicList() {
        viewModelScope.launch {
            _musicList.value = repository.getMusicList()
        }
    }
}