package com.example.artiumdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.artiumdemo.ui.components.BottomNavigation
import com.example.artiumdemo.ui.navigation.AudioListRoute
import com.example.artiumdemo.ui.navigation.audioListScreen
import com.example.artiumdemo.ui.navigation.audioRecorderScreen
import com.example.artiumdemo.ui.theme.ArtiumDemoTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtiumDemoTheme {
                AppNavigation()
            }
        }
    }
}


@Preview
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AudioListRoute,
            Modifier.padding(innerPadding)
        ) {
            audioListScreen{
            }
            audioRecorderScreen()
        }
    }
}