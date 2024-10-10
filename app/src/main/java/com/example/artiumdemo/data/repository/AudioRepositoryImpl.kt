package com.example.artiumdemo.data.repository

import com.example.artiumdemo.data.models.AudioFile
import com.example.artiumdemo.data.source.AudioManager
import com.example.artiumdemo.data.source.AudioRecorderDataSource
import com.example.artiumdemo.domain.repository.AudioRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject


class AudioRepositoryImpl @Inject constructor(
    private val audioRecorderDataSource: AudioRecorderDataSource,
    private val audioManager: AudioManager,
) : AudioRepository {

    override suspend fun startRecording(fileName: String) {
        audioRecorderDataSource.startRecording(fileName)
    }

    override suspend fun stopRecording(): File? {
        return audioRecorderDataSource.stopRecording()
    }

    override suspend fun pauseRecording() {
        audioRecorderDataSource.pauseRecording()
    }

    override fun getAmplitude(): Int {
        return audioRecorderDataSource.getAmplitude()
    }

    override fun getAllRecordedFiles(): Flow<List<AudioFile>> {
        return audioRecorderDataSource.getAllRecordedFiles()
    }

    override suspend fun loadAudioAmplitudes(audioPath: String): List<Int> {
        return audioManager.getAmplitudes(audioPath)
    }

}
