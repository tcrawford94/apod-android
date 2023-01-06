package com.trevorcrawford.apod.data

import com.trevorcrawford.apod.data.local.database.AstronomyPicture
import com.trevorcrawford.apod.data.local.database.AstronomyPictureDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface AstronomyPictureRepository {
    val astronomyPictures: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultAstronomyPictureRepository @Inject constructor(
    private val astronomyPictureDao: AstronomyPictureDao
) : AstronomyPictureRepository {

    override val astronomyPictures: Flow<List<String>> =
        astronomyPictureDao.getAstronomyPictures().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        astronomyPictureDao.insertAstronomyPicture(AstronomyPicture(name = name))
    }
}
