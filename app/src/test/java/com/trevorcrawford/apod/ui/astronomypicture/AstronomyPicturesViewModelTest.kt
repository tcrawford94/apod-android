package com.trevorcrawford.apod.ui.astronomypicture

import com.trevorcrawford.apod.data.fake.FakeAstronomyPictureRepository
import com.trevorcrawford.apod.data.fake.fakeAstronomyPictures
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import com.trevorcrawford.apod.ui.util.SnackbarManager
import com.trevorcrawford.apod.util.MainDispatcherRule
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // For runTest, remove when stable
class AstronomyPicturesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var viewModel: AstronomyPicturesViewModel

    @Before
    fun setup() {
        viewModel = AstronomyPicturesViewModel(
            astronomyPictureRepository = FakeAstronomyPictureRepository(),
            snackbarManager = SnackbarManager(),
        )
    }

    @Test
    fun viewModel_initial_uiState_has_emptyList() = runTest {
        // Given
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        // When nothing...

        // Then
        assertEquals(
            dataUiState.copy(previewList = persistentListOf()),
            viewModel.uiState.value
        )

        // Cleanup
        collectJob.cancel()
    }

    @Test
    fun viewModel_repositoryLoadPictures_loads_uiState_data() = runTest {
        // Given
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        // Starts with empty previewList
        assertEquals(
            dataUiState.copy(previewList = persistentListOf()),
            viewModel.uiState.value
        )

        // When
        viewModel.loadPictures()

        // Then
        assertEquals(
            dataUiState,
            viewModel.uiState.value
        )

        // Cleanup
        collectJob.cancel()
    }

    @Test
    fun viewModel_changeSortOption_resorts_uiState_previewList() = runTest {
        // Given
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        // When
        viewModel.loadPictures()
        viewModel.changeSortOption()

        // Then
        assertEquals(
            dataUiState.copy(
                previewList = dataUiState.previewList.sortedBy { it.date }.toPersistentList(),
                sortOrderRes = AstronomyPicturesViewModel.availableSortOptions[1].titleRes
            ),
            viewModel.uiState.value
        )

        // When sort option changed back
        viewModel.changeSortOption()

        // Then
        assertEquals(
            dataUiState.copy(
                previewList = dataUiState.previewList.sortedBy { it.title }.toPersistentList(),
                sortOrderRes = AstronomyPicturesViewModel.availableSortOptions[0].titleRes
            ),
            viewModel.uiState.value
        )

        // Cleanup
        collectJob.cancel()
    }
}

private val dataUiState = AstronomyPicturesUiState.Data(
    previewList = fakeAstronomyPictures.map {
        AstronomyPicturePreview(
            title = it.title,
            date = it.date,
            thumbnailUrl = it.url,
            copyright = it.copyright
        )
    }.toImmutableList(),
    sortOrderRes = AstronomyPicturesViewModel.availableSortOptions[0].titleRes,
    isRefreshing = false
)
