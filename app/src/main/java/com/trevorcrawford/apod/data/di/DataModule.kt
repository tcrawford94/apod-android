package com.trevorcrawford.apod.data.di

import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.data.OfflineFirstAstronomyPictureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsAstronomyPictureRepository(
        astronomyPictureRepository: OfflineFirstAstronomyPictureRepository
    ): AstronomyPictureRepository
}
