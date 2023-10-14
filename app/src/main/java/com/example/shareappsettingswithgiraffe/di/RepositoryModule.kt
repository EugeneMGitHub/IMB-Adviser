package com.example.shareappsettingswithgiraffe.di

import com.example.shareappsettingswithgiraffe.MainRepository
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object HiltModules {
    @Provides
    fun provideRepository(): MainRepository = MainRepository()
}