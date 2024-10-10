package com.example.artiumdemo.ui.screen.recorder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artiumdemo.R
import com.example.artiumdemo.ui.components.AmplitudeType
import com.example.artiumdemo.ui.components.AudioWaveform
import com.example.artiumdemo.ui.components.RoundedIcon
import com.example.artiumdemo.ui.components.WaveformAlignment
import com.example.artiumdemo.viewModel.AudioRecorderViewModel

@Composable
fun AudioRecorderScreen(viewModel: AudioRecorderViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Audio Recorder", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        AudioWaveform(
            modifier = Modifier.fillMaxWidth(),
            waveformAlignment = WaveformAlignment.Center,
            amplitudeType = AmplitudeType.Avg,
            progressBrush = Brush.horizontalGradient(listOf(Color(0xFF136FC3), Color(0xFF76EF66))),
            waveformBrush = SolidColor(Color.LightGray),
            amplitudes = state.amplitude,
            onProgressChange = {},
            onProgressChangeFinished = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        data class IconButtonData(val iconRes: Int, val contentDescription: String, val onClick: () -> Unit)

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            val buttons = when {
                state.isRecording -> listOf(
                    IconButtonData(R.drawable.ic_stop, "Stop") { viewModel.stopRecording() },
                    IconButtonData(R.drawable.ic_pause, "Pause") { viewModel.pauseRecording() }
                )
                state.isPaused -> listOf(
                    IconButtonData(R.drawable.ic_stop, "Stop") { viewModel.stopRecording() },
                    IconButtonData(R.drawable.ic_play, "Play") { viewModel.startRecording() }
                )
                else -> listOf(
                    IconButtonData(R.drawable.ic_play, "Start") { viewModel.startRecording() }
                )
            }

            buttons.forEach { buttonData ->
                RoundedIcon(buttonData.iconRes) {
                    buttonData.onClick()
                }
            }
        }

        if (state.isRecording) {
            Text(text = "Recording...", color = Color.Red)
        }
    }
}
