package com.trevorcrawford.apod.data

import com.trevorcrawford.apod.data.local.database.AstronomyPictureDao
import com.trevorcrawford.apod.data.local.database.model.RoomAstronomyPicture
import com.trevorcrawford.apod.data.local.database.model.asExternalModel
import com.trevorcrawford.apod.data.remote.PlanetaryNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

interface AstronomyPictureRepository {
    val astronomyPictures: Flow<List<AstronomyPicture>>
    suspend fun loadPictures(): Result<Any>
    suspend fun getPictureDetail(date: LocalDate): Flow<AstronomyPicture?>
}

class OfflineFirstAstronomyPictureRepository @Inject constructor(
    private val astronomyPictureDao: AstronomyPictureDao,
    private val network: PlanetaryNetworkDataSource
) : AstronomyPictureRepository {

    override val astronomyPictures: Flow<List<AstronomyPicture>> =
        astronomyPictureDao.getAstronomyPictures()
            .map { it.map(RoomAstronomyPicture::asExternalModel) }

    override suspend fun loadPictures(): Result<Any> {
        return network.getPictures()
            .onSuccess { networkResourceList ->
                val dbList: List<RoomAstronomyPicture> =
                    networkResourceList.filter {
                        it.isPicture
                    }.mapNotNull { networkAstronomyPicture ->
                        networkAstronomyPicture.date?.let { date ->
                            RoomAstronomyPicture(
                                title = networkAstronomyPicture.title ?: "",
                                explanation = networkAstronomyPicture.explanation ?: "",
                                date = date,
                                url = networkAstronomyPicture.url ?: "",
                                hdUrl = networkAstronomyPicture.hdUrl ?: "",
                                copyright = networkAstronomyPicture.copyright ?: ""
                            )
                        }
                    }

                astronomyPictureDao.clear()
                astronomyPictureDao.insert(dbList)
            }
            .onFailure {
                Timber.e(it.localizedMessage)
            }
    }

    override suspend fun getPictureDetail(date: LocalDate): Flow<AstronomyPicture?> =
        astronomyPictureDao.getAstronomyPicture(date.toString())
            .map { it?.asExternalModel() }
}
