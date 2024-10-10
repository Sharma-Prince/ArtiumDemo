package com.example.artiumdemo.domain.repository

import com.example.artiumdemo.data.models.AudioFile
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AudioRepository {
    suspend fun startRecording(fileName: String)
    suspend fun stopRecording(): File?
    suspend fun pauseRecording()
    fun getAmplitude(): Int
    fun getAllRecordedFiles(): Flow<List<AudioFile>>
    suspend fun loadAudioAmplitudes(audioPath: String): List<Int>
}
