package com.trevorcrawford.apod.ui.astronomypicture


import com.trevorcrawford.apod.data.AstronomyPicture
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.data.di.fakeAstronomyPictures
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import com.trevorcrawford.apod.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // For runTest, remove when stable
class AstronomyPictureViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun uiState_initially_loading() = runTest {
        // Given
        val viewModel = AstronomyPictureViewModel(FakeNoDataAstronomyPictureRepository())

        // When
        // nothing happens

        // Then
        assertEquals(AstronomyPictureUiState.Loading, viewModel.uiState.first())
    }

    @Test
    fun viewModel_on_init_pictures_loaded() = runTest {
        // Given
        val viewModel = AstronomyPictureViewModel(FakeAstronomyPictureRepository())

        // When
        // nothing happens

        // Then
        assertEquals(
            AstronomyPictureUiState.Data(
                previewList = fakeAstronomyPictures.map {
                    AstronomyPicturePreview(
                        title = it.title,
                        date = it.date,
                        thumbnailUrl = it.url
                    )
                },
                sortOrderRes = AstronomyPictureViewModel.availableSortOptions.first().titleRes
            ),
            viewModel.uiState.first()
        )
    }
}

private class FakeNoDataAstronomyPictureRepository : AstronomyPictureRepository {


    override val astronomyPictures: Flow<List<AstronomyPicture>>
        get() = flow { delay(500) }

    override suspend fun loadPictures(): Result<Any> {
        return Result.success("no data")
    }
}


private class FakeAstronomyPictureRepository : AstronomyPictureRepository {

    private val data = mutableListOf<AstronomyPicture>()

    override val astronomyPictures: Flow<List<AstronomyPicture>>
        get() = flow { emit(data.toList()) }

    override suspend fun loadPictures(): Result<Any> {
        data.clear()

        val newList = fakeAstronomyPictures
        data.addAll(newList)
        return Result.success(newList)
    }
}
