package com.example.artiumdemo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artiumdemo.domain.usecase.AudioRecordingUseCase
import com.example.artiumdemo.domain.usecase.RecordingAction
import com.example.artiumdemo.ui.screen.recorder.AudioRecorderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AudioRecorderViewModel @Inject constructor(
    private val audioRecordingUseCase: AudioRecordingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AudioRecorderState())
    val state: StateFlow<AudioRecorderState> = _state

    private var amplitudeJob: Job? = null

    fun startRecording() {
        val fileName = "recording_${System.currentTimeMillis()}"
        viewModelScope.launch {
            audioRecordingUseCase(RecordingAction.Start(fileName))
            _state.value = _state.value.copy(
                isRecording = true,
                isPaused = false,
                amplitude = emptyList()
            )
            startUpdatingAmplitude()
        }
    }
    private fun startUpdatingAmplitude() {
        amplitudeJob = viewModelScope.launch {
            while (_state.value.isRecording) {
                val currentAmplitude = audioRecordingUseCase.getAmplitude()
                _state.value = _state.value.copy(amplitude = _state.value.amplitude + currentAmplitude)
                delay(100)
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            audioRecordingUseCase(RecordingAction.Stop)
            _state.value = _state.value.copy(isRecording = false, isPaused = false)
        }
    }

    fun pauseRecording() {
        viewModelScope.launch {
            audioRecordingUseCase(RecordingAction.Pause)
            _state.value = _state.value.copy(isRecording = false, isPaused = true)
        }
    }

}