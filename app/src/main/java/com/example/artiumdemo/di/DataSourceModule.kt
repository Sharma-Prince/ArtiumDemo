package com.example.artiumdemo.di

import android.content.Context
import com.example.artiumdemo.data.source.AudioRecorderDataSource
import com.example.artiumdemo.data.source.AudioRecorderDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import linc.com.amplituda.Amplituda
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {


    @Provides
    @Singleton
    fun provideAudioRecorderDataSource(
        @ApplicationContext context: Context
    ): AudioRecorderDataSource {
        return AudioRecorderDataSourceImpl(context)
    }

    @Provides
    fun provideCoroutineContext(): CoroutineDispatcher = Dispatchers.IO


    @Provides
    fun provideAmplituda(@ApplicationContext context: Context): Amplituda = Amplituda(context)
}
