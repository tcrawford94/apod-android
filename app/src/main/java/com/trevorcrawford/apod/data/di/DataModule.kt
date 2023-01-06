package com.trevorcrawford.apod.data.di

import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.data.DefaultAstronomyPictureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsAstronomyPictureRepository(
        astronomyPictureRepository: DefaultAstronomyPictureRepository
    ): AstronomyPictureRepository
}

class FakeAstronomyPictureRepository @Inject constructor() : AstronomyPictureRepository {
    override val astronomyPictures: Flow<List<String>> = flowOf(fakeAstronomyPictures)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }
}

val fakeAstronomyPictures = listOf("One", "Two", "Three")
