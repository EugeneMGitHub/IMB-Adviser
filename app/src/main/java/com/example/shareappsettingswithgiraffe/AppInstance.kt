package com.example.shareappsettingswithgiraffe

import android.app.Application

import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import androidx.work.Configuration
import androidx.work.WorkManager


@HiltAndroidApp
class AppInstance : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

