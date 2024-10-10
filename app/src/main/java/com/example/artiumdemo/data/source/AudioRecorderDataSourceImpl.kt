package com.example.artiumdemo.data.source

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import com.example.artiumdemo.data.models.AudioFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject

class AudioRecorderDataSourceImpl @Inject constructor(
    private val context: Context
) : AudioRecorderDataSource {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private val _recordedFilesFlow = MutableStateFlow<List<AudioFile>>(emptyList())

    init {
        loadExistingAudioFiles()
    }

    private fun loadExistingAudioFiles() {
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        outputDir?.listFiles { file ->
            file.extension == "mp3"
        }?.forEach { file ->
            val audioFile = AudioFile(
                id = _recordedFilesFlow.value.size.toLong(),
                name = file.name,
                uri = Uri.fromFile(file),
                duration = getAudioDuration(file)
            )
            _recordedFilesFlow.value += audioFile
        }
    }
    override suspend fun startRecording(fileName: String) {
        val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        outputFile = File(outputDir, "$fileName.mp3")

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile?.absolutePath)
            prepare()
            start()
        }
    }

    override suspend fun stopRecording(): File? {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        outputFile?.let {
            _recordedFilesFlow.emit(_recordedFilesFlow.value + AudioFile(
                id = _recordedFilesFlow.value.size.toLong(),
                name = it.name,
                uri = Uri.fromFile(it),
                duration = getAudioDuration(it)
            ))
        }
        return outputFile
    }

    override suspend fun pauseRecording() {
        recorder?.pause()
    }

    override fun getAmplitude(): Int {
        return recorder?.maxAmplitude ?: 0
    }

    override fun getAllRecordedFiles(): Flow<List<AudioFile>> {
        return _recordedFilesFlow
    }

    private fun getAudioDuration(file: File): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(file.path)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
        retriever.release()
        return duration ?: 0L
    }
}
