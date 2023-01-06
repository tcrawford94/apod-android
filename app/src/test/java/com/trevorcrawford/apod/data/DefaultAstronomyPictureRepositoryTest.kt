package com.trevorcrawford.apod.data

import com.trevorcrawford.apod.data.local.database.AstronomyPicture
import com.trevorcrawford.apod.data.local.database.AstronomyPictureDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DefaultAstronomyPictureRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultAstronomyPictureRepositoryTest {

    @Test
    fun astronomyPictures_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultAstronomyPictureRepository(FakeAstronomyPictureDao())

        repository.add("Repository")

        assertEquals(repository.astronomyPictures.first().size, 1)
    }

}

private class FakeAstronomyPictureDao : AstronomyPictureDao {

    private val data = mutableListOf<AstronomyPicture>()

    override fun getAstronomyPictures(): Flow<List<AstronomyPicture>> = flow {
        emit(data)
    }

    override suspend fun insertAstronomyPicture(item: AstronomyPicture) {
        data.add(0, item)
    }
}
