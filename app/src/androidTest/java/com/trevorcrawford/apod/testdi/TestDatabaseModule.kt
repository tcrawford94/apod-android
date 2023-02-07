package com.trevorcrawford.apod.testdi

import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.data.di.DataModule
import com.trevorcrawford.apod.data.fake.FakeAstronomyPictureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface FakeDataModule {
    @Binds
    abstract fun bindRepository(
        fakeRepository: FakeAstronomyPictureRepository
    ): AstronomyPictureRepository
}
