package com.trevorcrawford.apod.data.fake

import com.trevorcrawford.apod.data.AstronomyPicture
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.net.UnknownHostException
import java.time.LocalDate
import javax.inject.Inject

open class FakeAstronomyPictureRepository @Inject constructor() : AstronomyPictureRepository {
    override val astronomyPictures: Flow<List<AstronomyPicture>> = flowOf(fakeAstronomyPictures)
    override val isRefreshingPictures: Flow<Boolean> = flowOf(false)
    override suspend fun loadPictures(): Result<Any> {
        return Result.success(fakeAstronomyPictures)
    }

    override suspend fun getPictureDetail(date: LocalDate): Flow<AstronomyPicture?> =
        flowOf(fakeAstronomyPictures.first())
}

class FakeOfflineAstronomyPictureRepository : FakeAstronomyPictureRepository() {
    override val astronomyPictures: Flow<List<AstronomyPicture>>
        get() = flow {}

    override suspend fun loadPictures(): Result<Any> {
        return Result.failure(UnknownHostException("Please check your network connection and try again."))
    }

    override suspend fun getPictureDetail(date: LocalDate): Flow<AstronomyPicture?> =
        flowOf(null)
}

val fakeAstronomyPictures = listOf(
    AstronomyPicture(
        title = "Europa: Oceans of Life?",
        explanation = "Is there life beneath Europa's frozen surface? Some believe the oceans found there of carbon-enriched water are the best chance for life, outside the Earth, in our Solar System. Europa, the fourth largest moon of Jupiter, was recently discovered to have a thin oxygen atmosphere by scientists using the Hubble Space Telescope. Although Earth's atmospheric abundance of oxygen is indicative of life, astronomers speculate that Europa's oxygen arises purely from physical processes. But what an interesting coincidence! The above picture was taken by a Voyager spacecraft in 1979, but the spacecraft Galileo is currently circling Jupiter and has been photographing Europa. The first of these pictures will be released two days from today. Will they show the unexpected?  Late News: NASA to Release New Europa and Jupiter Pictures Thursday  More Late News: New Evidence Suggests Microscopic Life Existed on Mars",
        date = LocalDate.parse("1996-08-06"),
        hdUrl = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_2500.jpg",
        url = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_960.jpg",
        copyright = "Test Copyright"
    ),
    AstronomyPicture(
        title = "Highlights of the Summer Sky",
        explanation = "What's up in the sky this summer? The featured graphic gives a few highlights for Earth's northern hemisphere. Viewed as a clock face centered at the bottom, early summer sky events fan out toward the left, while late summer events are projected toward the right. Objects relatively close to Earth are illustrated, in general, as nearer to the cartoon figure with the telescope at the bottom center -- although almost everything pictured can be seen without a telescope.  Highlights of this summer's sky include that Jupiter will be visible after sunset during June, while Saturn will be visible after sunset during August.  A close grouping of the Moon, Venus and the bright star Aldebaran will occur during mid-July. In early August, the Perseids meteor shower peaks.  Surely the most famous pending astronomical event occurring this summer, though, will be a total eclipse of the Sun visible over a thin cloud-free swath across the USA on 21 August.    Follow APOD on: Facebook,  Google Plus,  Instagram, or Twitter",
        date = LocalDate.parse("2017-06-05"),
        hdUrl = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_2500.jpg",
        url = "https://apod.nasa.gov/apod/image/1706/Summer2017Sky_universe2go_960.jpg",
        copyright = "Test Copyright2"
    ),
    AstronomyPicture(
        title = "The Milky Way",
        explanation = "asdfjklasdfjkl",
        date = LocalDate.now(),
        url = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
        hdUrl = "https://apod.nasa.gov/apod/image/0712/ic1396_wood.jpg",
        copyright = "Test Copyright3"
    )
)
