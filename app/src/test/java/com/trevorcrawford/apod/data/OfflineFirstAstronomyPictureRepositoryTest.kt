package com.trevorcrawford.apod.data

import com.trevorcrawford.apod.data.local.database.AstronomyPictureDao
import com.trevorcrawford.apod.data.local.database.model.RoomAstronomyPicture
import com.trevorcrawford.apod.data.local.database.model.asExternalModel
import com.trevorcrawford.apod.data.remote.PlanetaryNetworkDataSource
import com.trevorcrawford.apod.data.remote.retrofit.model.NetworkAstronomyResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for [OfflineFirstAstronomyPictureRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // For runTest, remove when stable
class OfflineFirstAstronomyPictureRepositoryTest {

    @Test
    fun repository_astronomy_pictures_stream_is_backed_by_astronomy_pictures_dao_and_network_data_source_and_returns_results() =
        runTest {
            // Given FakeNetworkWithResult
            val astronomyPictureDao = FakeAstronomyPictureDao()
            val networkDataSource = FakeNetworkWithResult()
            val repository = OfflineFirstAstronomyPictureRepository(
                astronomyPictureDao = astronomyPictureDao,
                network = networkDataSource
            )

            // When
            repository.loadPictures()

            // Then
            assertEquals(
                astronomyPictureDao.getAstronomyPictures().first()
                    .map(RoomAstronomyPicture::asExternalModel),
                repository.astronomyPictures.first()
            )
        }

    @Test
    fun repository_astronomy_pictures_stream_returns_empty_list() = runTest {
        // Given FakeNetworkWithEmptyResult
        val astronomyPictureDao = FakeAstronomyPictureDao()
        val networkDataSource = FakeNetworkWithResult()
        val repository = OfflineFirstAstronomyPictureRepository(
            astronomyPictureDao = astronomyPictureDao,
            network = networkDataSource
        )

        // When
        repository.loadPictures()

        // Then
        assertEquals(
            astronomyPictureDao.getAstronomyPictures().first()
                .map(RoomAstronomyPicture::asExternalModel),
            repository.astronomyPictures.first()
        )
    }

    @Test
    fun repository_multiple_loads_overwrites_old_data() = runTest {
        // Given FakeNetworkWithResult
        val astronomyPictureDao = FakeAstronomyPictureDao()
        val networkDataSource = FakeNetworkWithEmptyResult()
        val repository = OfflineFirstAstronomyPictureRepository(
            astronomyPictureDao = astronomyPictureDao,
            network = networkDataSource
        )

        // When loadPictures Twice
        repository.loadPictures()
        repository.loadPictures()

        // Then
        assertEquals(
            astronomyPictureDao.getAstronomyPictures().first()
                .map(RoomAstronomyPicture::asExternalModel),
            repository.astronomyPictures.first()
        )
    }

    @Test
    fun repository_load_pictures_updates_refreshing_pictures_flag_to_true() = runTest {
        // Given FakeNetworkWithResult
        val astronomyPictureDao = FakeAstronomyPictureDao()
        val networkDataSource = FakeNetworkWithResult()
        val repository = OfflineFirstAstronomyPictureRepository(
            astronomyPictureDao = astronomyPictureDao,
            network = networkDataSource
        )
        val isRefreshingPicturesValues = mutableListOf<Boolean>()
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.isRefreshingPictures.toList(isRefreshingPicturesValues)
        }

        // When loadPictures
        repository.loadPictures()

        // Then
        assertEquals(
            listOf(false, true, false),
            isRefreshingPicturesValues
        )

        collectJob.cancel()
    }
}

private class FakeAstronomyPictureDao : AstronomyPictureDao {

    private val data = mutableListOf<RoomAstronomyPicture>()

    override fun getAstronomyPictures(): Flow<List<RoomAstronomyPicture>> = flow {
        emit(data)
    }

    override suspend fun insert(pictures: List<RoomAstronomyPicture>) {
        data.addAll(pictures)
    }

    override suspend fun clear() {
        data.clear()
    }
}

private class FakeNetworkWithResult : PlanetaryNetworkDataSource {
    override suspend fun getPictures(): Result<List<NetworkAstronomyResource>> {
        return Result.success(
            listOf(
                NetworkAstronomyResource(
                    title = "Highlights of the Summer Sky",
                    explanation = "What's up in the sky this summer? The featured graphic gives a few highlights for Earth's northern hemisphere. Viewed as a clock face centered at the bottom, early summer sky events fan out toward the left, while late summer events are projected toward the right. Objects relatively close to Earth are illustrated, in general, as nearer to the cartoon figure with the telescope at the bottom center -- although almost everything pictured can be seen without a telescope.  Highlights of this summer's sky include that Jupiter will be visible after sunset during June, while Saturn will be visible after sunset during August.  A close grouping of the Moon, Venus and the bright star Aldebaran will occur during mid-July. In early August, the Perseids meteor shower peaks.  Surely the most famous pending astronomical event occurring this summer, though, will be a total eclipse of the Sun visible over a thin cloud-free swath across the USA on 21 August.    Follow APOD on: Facebook,  Google Plus,  Instagram, or Twitter",
                    date = LocalDate.parse("2017-06-05"),
                    mediaType = "image",
                    hdUrl = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_2500.jpg",
                    url = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_960.jpg"
                ),
                NetworkAstronomyResource(
                    title = "Europa: Oceans of Life?",
                    explanation = "Is there life beneath Europa's frozen surface? Some believe the oceans found there of carbon-enriched water are the best chance for life, outside the Earth, in our Solar System. Europa, the fourth largest moon of Jupiter, was recently discovered to have a thin oxygen atmosphere by scientists using the Hubble Space Telescope. Although Earth's atmospheric abundance of oxygen is indicative of life, astronomers speculate that Europa's oxygen arises purely from physical processes. But what an interesting coincidence! The above picture was taken by a Voyager spacecraft in 1979, but the spacecraft Galileo is currently circling Jupiter and has been photographing Europa. The first of these pictures will be released two days from today. Will they show the unexpected?  Late News: NASA to Release New Europa and Jupiter Pictures Thursday  More Late News: New Evidence Suggests Microscopic Life Existed on Mars",
                    date = LocalDate.parse("1996-08-06"),
                    mediaType = "image",
                    hdUrl = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_2500.jpg",
                    url = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_960.jpg"
                )
            )
        )
    }
}

private class FakeNetworkWithEmptyResult : PlanetaryNetworkDataSource {
    override suspend fun getPictures(): Result<List<NetworkAstronomyResource>> {
        return Result.success(listOf())
    }
}
