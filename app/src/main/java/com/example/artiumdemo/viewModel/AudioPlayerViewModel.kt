package com.example.artiumdemo.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artiumdemo.data.models.AudioFile
import com.example.artiumdemo.domain.repository.AudioRepository
import com.example.artiumdemo.media.AudioPlaybackManager
import com.example.artiumdemo.ui.screen.audioplayer.AudioWaveformUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val playbackManager: AudioPlaybackManager
) : ViewModel() {

    private var currentLocalAudio: AudioFile? = null

    var uiState: AudioWaveformUiState by mutableStateOf(AudioWaveformUiState())
        private set

    private val _audioList = MutableStateFlow<List<AudioFile>>(emptyList())
    val audioList: StateFlow<List<AudioFile>> = _audioList

    init {
        playbackManager.initializeController()
        fetchRecordedAudioList()
    }
    override fun onCleared() {
        super.onCleared()
        playbackManager.releaseController()
    }

    private fun fetchRecordedAudioList() {
        viewModelScope.launch {
            audioRepository.getAllRecordedFiles().collect { audioFiles ->
                _audioList.value = audioFiles
            }
        }
    }
    private fun updatePlaybackProgress(position: Long) {
        val audio = currentLocalAudio ?: return
        uiState = uiState.copy(progress = position.toFloat() / audio.duration)
    }

    private fun updatePlayingState(isPlaying: Boolean) {
        uiState = uiState.copy(isPlaying = isPlaying)
    }
    fun loadAudio(audioFile: AudioFile) {
        viewModelScope.launch {
            try {
                currentLocalAudio = audioFile
                currentLocalAudio?.let(playbackManager::setAudio)

                launch { currentLocalAudio?.uri?.path?.let { loadAudioAmplitudes(it) } }
                launch { observePlaybackEvents() }
                uiState = uiState.copy(
                    audioDisplayName = currentLocalAudio?.nameWithoutExtension.orEmpty(),
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun observePlaybackEvents() {
        playbackManager.events.collectLatest {
            when(it) {
                is AudioPlaybackManager.Event.PositionChanged -> updatePlaybackProgress(it.position)
                is AudioPlaybackManager.Event.PlayingChanged -> updatePlayingState(it.isPlaying)
            }
        }
    }

    private suspend fun loadAudioAmplitudes(localPath: String) {
        try {
            val amplitudes = audioRepository.loadAudioAmplitudes(localPath)
            uiState = uiState.copy(amplitudes = amplitudes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun updatePlaybackState() {
        when {
            uiState.isPlaying -> playbackManager.pause()
            else -> playbackManager.play()
        }
    }
    fun updateProgress(progress: Float) {
        val position = currentLocalAudio?.duration?.times(progress)?.toLong() ?: 0L
        playbackManager.seekTo(position)
        uiState = uiState.copy(progress = progress)
    }

}