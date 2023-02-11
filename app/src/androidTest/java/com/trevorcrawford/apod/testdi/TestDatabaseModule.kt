package com.trevorcrawford.apod.testdi

import com.trevorcrawford.apod.data.AstronomyPicture
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.data.di.DataModule
import com.trevorcrawford.apod.data.fake.FakeAstronomyPictureRepository
import com.trevorcrawford.apod.data.fake.fakeAstronomyPictures
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface FakeDataModule {
    @Binds
    fun bindRepository(
        fakeRepository: DummyUiTestAstronomyPictureRepository
    ): AstronomyPictureRepository
}

// Dummy AstronomyPictureRepository for UiTest that already has data backing astronomyPictures
// flow for fast and reliable UI tests
class DummyUiTestAstronomyPictureRepository @Inject constructor() :
    FakeAstronomyPictureRepository() {
    override val astronomyPictures: Flow<List<AstronomyPicture>>
        get() = flowOf(fakeAstronomyPictures)
}
