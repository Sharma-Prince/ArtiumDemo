package com.example.artiumdemo.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.artiumdemo.ui.screen.audioplayer.AudioPlayerScreen
import kotlinx.serialization.Serializable

@Serializable object AudioListRoute


fun NavGraphBuilder.audioListScreen(
    onAudioClick : (String) -> Unit
) {
    composable<AudioListRoute> {
        AudioPlayerScreen()
    }
}