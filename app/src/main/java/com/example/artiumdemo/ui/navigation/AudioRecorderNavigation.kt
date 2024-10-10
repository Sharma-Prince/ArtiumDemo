package com.example.artiumdemo.ui.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.artiumdemo.ui.screen.recorder.AudioRecorderScreen
import com.example.artiumdemo.utils.Permissions
import kotlinx.serialization.Serializable


@Serializable
object AudioRecorderRoute

fun NavController.navigateRecorderScreen(navOptions: NavOptions) = navigate(route = AudioRecorderRoute, navOptions)

fun NavGraphBuilder.audioRecorderScreen() {
    composable<AudioRecorderRoute> {
        val context = LocalContext.current
        var hasPermission by remember { mutableStateOf(Permissions().hasRecordAudioPermission(context)) }
        val permissionLauncher: ActivityResultLauncher<String> = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            hasPermission = isGranted
        }

        if (!hasPermission) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Audio recording permission is required.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                }) {
                    Text("Request Permission")
                }
            }
        } else {
            AudioRecorderScreen()
        }
    }
}