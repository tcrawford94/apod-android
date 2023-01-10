package com.trevorcrawford.apod.data.remote

import com.trevorcrawford.apod.data.remote.retrofit.model.NetworkAstronomyResource

/**
 * Interface representing network calls to the Planetary backend
 */
interface PlanetaryNetworkDataSource {
    suspend fun getPictures(): Result<List<NetworkAstronomyResource>>
}