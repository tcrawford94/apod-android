package com.trevorcrawford.apod.ui.astronomypicture


import com.trevorcrawford.apod.data.AstronomyPicture
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.data.di.fakeAstronomyPictures
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import com.trevorcrawford.apod.ui.util.SnackbarManager
import com.trevorcrawford.apod.util.MainDispatcherRule
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // For runTest, remove when stable
class AstronomyPicturesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun uiState_initially_loading() = runTest {
        // Given
        val viewModel =
            AstronomyPicturesViewModel(
                astronomyPictureRepository = FakeOfflineAstronomyPictureRepository(),
                snackbarManager = SnackbarManager()
            )

        // When
        // nothing happens

        // Then
        assertEquals(AstronomyPicturesUiState.Loading, viewModel.uiState.first())
    }

    @Test
    fun viewModel_on_init_pictures_loaded() = runTest {
        // Given
        val viewModel = AstronomyPicturesViewModel(
            astronomyPictureRepository = FakeAstronomyPictureRepository(),
            snackbarManager = SnackbarManager()
        )

        // When
        // nothing happens, since in init block of viewmodel

        // Then
        assertEquals(
            AstronomyPicturesUiState.Data(
                previewList = fakeAstronomyPictures.map {
                    AstronomyPicturePreview(
                        title = it.title,
                        date = it.date,
                        thumbnailUrl = it.url,
                        copyright = it.copyright
                    )
                }.toImmutableList(),
                sortOrderRes = AstronomyPicturesViewModel.availableSortOptions.first().titleRes,
                isRefreshing = false
            ),
            viewModel.uiState.first()
        )
    }
}

private class FakeOfflineAstronomyPictureRepository : FakeAstronomyPictureRepository() {
    override val astronomyPictures: Flow<List<AstronomyPicture>>
        get() = flow {}

    override suspend fun loadPictures(): Result<Any> {
        return Result.failure(UnknownHostException("Please check your network connection and try again."))
    }

    override suspend fun getPictureDetail(date: LocalDate): Flow<AstronomyPicture> {
        TODO("Not yet implemented")
    }
}

private open class FakeAstronomyPictureRepository : AstronomyPictureRepository {
    private val data = mutableListOf<AstronomyPicture>()

    override val astronomyPictures: Flow<List<AstronomyPicture>>
        get() = flow { emit(data.toList()) }

    override val isRefreshingPictures: Flow<Boolean>
        get() = flow { emit(false) }

    override suspend fun loadPictures(): Result<Any> {
        data.clear()
        data.addAll(fakeAstronomyPictures)
        return Result.success(true)
    }

    override suspend fun getPictureDetail(date: LocalDate): Flow<AstronomyPicture> {
        TODO("Not yet implemented")
    }
}
