package com.example.artiumdemo.ui.screen.audioplayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.artiumdemo.R
import com.example.artiumdemo.data.models.AudioFile
import com.example.artiumdemo.ui.components.AmplitudeType
import com.example.artiumdemo.ui.components.AudioWaveform
import com.example.artiumdemo.ui.components.RoundedIcon
import com.example.artiumdemo.ui.components.WaveformAlignment
import com.example.artiumdemo.utils.formatAsFileSize
import com.example.artiumdemo.viewModel.AudioPlayerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerScreen(viewModel: AudioPlayerViewModel = hiltViewModel()) {
    val audioList by viewModel.audioList.collectAsState(initial = emptyList())
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recorded Audios") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (audioList.isEmpty()) {
                Text(
                    text = "No recordings available.",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            } else {
                LazyColumn {
                    items(audioList) { audioFile ->
                        AudioListItem(audioFile) {
                            showBottomSheet = true
                            viewModel.loadAudio(audioFile)
                        }
                    }
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
                sheetState = sheetState,
            ) {
                BottomSheetAudio(
                    uiState = viewModel.uiState,
                    onPlayClicked = viewModel::updatePlaybackState,
                    onProgressChange = viewModel::updateProgress
                )
            }
        }
    }
}

@Composable
fun BottomSheetAudio(
    uiState: AudioWaveformUiState,
    onPlayClicked: () -> Unit,
    onProgressChange: (Float) -> Unit,
){
    var scrollEnabled by remember { mutableStateOf(true) }
    val playButtonIcon by remember(uiState.isPlaying) {
        mutableIntStateOf(if(uiState.isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
    }
    Column(
        modifier = Modifier.heightIn(400.dp)
    ) {
        AudioWaveform(
            modifier = Modifier.fillMaxWidth(),
            style = Fill,
            waveformAlignment = WaveformAlignment.Center,
            amplitudeType = AmplitudeType.Avg,
            progressBrush = Brush.horizontalGradient(listOf(Color(0xFF136FC3), Color(0xFF76EF66))),
            waveformBrush = SolidColor(Color.LightGray),
            spikeWidth = Dp(4f),
            spikePadding = Dp(2f),
            spikeRadius = Dp(2f),
            progress = uiState.progress,
            amplitudes = uiState.amplitudes,
            onProgressChange = {
                scrollEnabled = false
                onProgressChange(it)
            },
            onProgressChangeFinished = {
                scrollEnabled = true
            }
        )
        Spacer(modifier = Modifier.width(34.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            RoundedIcon(playButtonIcon){
                onPlayClicked()
            }
        }
    }
}

@Composable
fun AudioListItem(audioFile: AudioFile, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable { onClick() }
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audioFile.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = audioFile.duration.formatAsFileSize,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
