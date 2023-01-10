package com.trevorcrawford.apod.data.remote.retrofit

import com.trevorcrawford.apod.BuildConfig
import com.trevorcrawford.apod.data.remote.PlanetaryNetworkDataSource
import com.trevorcrawford.apod.data.remote.retrofit.model.NetworkAstronomyResource
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import timber.log.Timber
import java.net.UnknownHostException


/**
 * Retrofit API declaration for Planetary Network API
 */
private interface RetrofitPlanetaryNetworkApi {
    /**
     * APOD - Astronomy Picture of the day.
     * See [the docs](https://api.nasa.gov/) and [github micro service](https://github.com/nasa/apod-api#docs-)
     */
    @GET("planetary/apod?count=20&api_key=${BuildConfig.API_KEY}")
    suspend fun getPictures(): Response<List<NetworkAstronomyResource>>
}


class RetrofitPlanetaryNetwork(retrofit: Retrofit) : PlanetaryNetworkDataSource {

    private val networkApi: RetrofitPlanetaryNetworkApi =
        retrofit.create(RetrofitPlanetaryNetworkApi::class.java)

    override suspend fun getPictures(): Result<List<NetworkAstronomyResource>> =
        callSafely { networkApi.getPictures() }

    private suspend fun <T> callSafely(block: suspend () -> Response<T>): Result<T & Any> {
        val response = try {
            block()
        } catch (e: UnknownHostException) {
            return Result.failure(UnknownHostException("Please check your network connection and try again.").log())
        } catch (e: HttpException) {
            return Result.failure(e.log())
        } catch (e: Exception) { // We have no type information about what's bubbling up from Moshi and Retrofit
            return Result.failure(e.log())
        }

        val body = response.body()

        return if (response.isSuccessful && body != null) {
            Result.success(body)
        } else {
            val errorBody = response.errorBody()
            if (errorBody != null) {
                Result.failure(PlanetaryApiException(errorBody.toString()).log())
            } else {
                Result.failure(PlanetaryApiException(response.message()).log())
            }
        }
    }

    private fun Exception.log(): Exception {
        Timber.e(this.localizedMessage)
        return this
    }
}

class PlanetaryApiException(apiMessage: String) : RuntimeException(apiMessage)
