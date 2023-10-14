package com.example.shareappsettingswithgiraffe.di

import android.app.Application
import androidx.room.Room
import com.example.shareappsettingswithgiraffe.MainRepository
import com.example.shareappsettingswithgiraffe.data.database.AppDataBase
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepository
import com.example.shareappsettingswithgiraffe.data.database.DataBaseRepositoryIm
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)


object DataBaseModule {

    @Provides
    @Singleton
    fun provideAppDataBase(app: Application): AppDataBase {
        return Room.databaseBuilder(
            app,
            AppDataBase::class.java,
            "app_database"
        )
            .addMigrations(AppDataBase.migration3To4)
            .build()
    }


    @Provides
    @Singleton
    fun provideDataBaseRepository(db: AppDataBase): DataBaseRepository {
        return DataBaseRepositoryIm(db.dataBaseDao)
    }

}


