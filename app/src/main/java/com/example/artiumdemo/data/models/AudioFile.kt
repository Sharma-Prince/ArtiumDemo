package com.example.artiumdemo.data.models

import android.net.Uri

data class AudioFile(
    val id: Long,
    val name: String,
    val uri: Uri,
    val duration: Long
){
    val nameWithoutExtension: String get() = name.substringBeforeLast('.')
}
