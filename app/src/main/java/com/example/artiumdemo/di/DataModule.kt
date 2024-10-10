package com.example.artiumdemo.di

import com.example.artiumdemo.domain.repository.AudioRepository
import com.example.artiumdemo.data.repository.AudioRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindAudioRepository(
        topicsRepository: AudioRepositoryImpl,
    ): AudioRepository

}