package com.trevorcrawford.apod.data.remote.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trevorcrawford.apod.BuildConfig
import com.trevorcrawford.apod.data.remote.PlanetaryNetworkDataSource
import com.trevorcrawford.apod.data.remote.retrofit.RetrofitPlanetaryNetwork
import com.trevorcrawford.apod.data.remote.retrofit.model.DayAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providePlanetaryNetworkDataSource(retrofit: Retrofit): PlanetaryNetworkDataSource {
        return RetrofitPlanetaryNetwork(retrofit)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .add(DayAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.NASA_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}