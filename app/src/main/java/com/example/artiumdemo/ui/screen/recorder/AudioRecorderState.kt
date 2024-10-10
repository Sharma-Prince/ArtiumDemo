package com.example.artiumdemo.ui.screen.recorder

data class AudioRecorderState(
    val isRecording: Boolean = false,
    val isPaused: Boolean = false,
    val amplitude: List<Int> = emptyList()
)