package com.trevorcrawford.apod.data.local.di

import android.content.Context
import androidx.room.Room
import com.trevorcrawford.apod.data.local.database.AppDatabase
import com.trevorcrawford.apod.data.local.database.AstronomyPictureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideAstronomyPictureDao(appDatabase: AppDatabase): AstronomyPictureDao {
        return appDatabase.astronomyPictureDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "AstronomyPicture"
        ).fallbackToDestructiveMigration().build()
    }
}
