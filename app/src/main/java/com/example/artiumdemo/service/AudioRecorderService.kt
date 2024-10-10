package com.example.artiumdemo.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.artiumdemo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioRecorderService : Service() {

    private lateinit var mediaRecorder: MediaRecorder
    private var isRecording: Boolean = false
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): AudioRecorderService = this@AudioRecorderService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    fun startRecording(outputFilePath: String) {
        if (!isRecording) {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFilePath)
                prepare()
                start()
            }
            isRecording = true
        }
    }

    fun pauseRecording() {
        if (isRecording && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mediaRecorder.pause()
        }
    }

    fun stopRecording() {
        if (isRecording) {
            mediaRecorder.apply {
                stop()
                reset()
                release()
            }
            isRecording = false
        }
    }

    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Audio Recorder")
            .setContentText("Recording audio")
            .setSmallIcon(R.drawable.ic_audio_recorder)
            .addAction(R.drawable.ic_pause, "Pause", getPendingIntent(ACTION_PAUSE))
            .addAction(R.drawable.ic_stop, "Stop", getPendingIntent(ACTION_STOP))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        return notificationBuilder.build()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, AudioRecorderService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        const val NOTIFICATION_ID = 2
        const val CHANNEL_ID = "audio_recorder_channel"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_STOP = "action_stop"
    }
}
