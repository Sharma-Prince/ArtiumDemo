package com.example.artiumdemo.data.source

import com.example.artiumdemo.data.models.AudioFile
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AudioRecorderDataSource {
    suspend fun startRecording(fileName: String)
    suspend fun stopRecording(): File?
    suspend fun pauseRecording()
    fun getAmplitude(): Int
    fun getAllRecordedFiles(): Flow<List<AudioFile>>
}
