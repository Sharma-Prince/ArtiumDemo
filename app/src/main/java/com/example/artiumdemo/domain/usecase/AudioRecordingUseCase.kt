package com.example.artiumdemo.domain.usecase

import com.example.artiumdemo.data.models.AudioFile
import com.example.artiumdemo.domain.repository.AudioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AudioRecordingUseCase @Inject constructor(
    private val audioRepository: AudioRepository
) {
    suspend operator fun invoke(action: RecordingAction) {
        when (action) {
            is RecordingAction.Start -> audioRepository.startRecording(action.fileName)
            is RecordingAction.Stop -> audioRepository.stopRecording()
            is RecordingAction.Pause -> audioRepository.pauseRecording()
        }
    }
    suspend fun getAmplitude(): Int {
        return audioRepository.getAmplitude()
    }
    suspend fun getAllRecordedFiles(): Flow<List<AudioFile>> {
        return audioRepository.getAllRecordedFiles()
    }
}

sealed class RecordingAction {
    data class Start(val fileName: String) : RecordingAction()
    object Stop : RecordingAction()
    object Pause : RecordingAction()
}
