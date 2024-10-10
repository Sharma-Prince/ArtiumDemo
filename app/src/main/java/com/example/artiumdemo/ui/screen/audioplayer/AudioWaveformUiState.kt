package com.example.artiumdemo.ui.screen.audioplayer

data class AudioWaveformUiState(
    val audioDisplayName: String = "",
    val amplitudes: List<Int> = emptyList(),
    val isPlaying: Boolean = false,
    val progress: Float = 0F
)