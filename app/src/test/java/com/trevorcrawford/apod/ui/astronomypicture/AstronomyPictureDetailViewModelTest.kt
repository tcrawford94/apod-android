package com.trevorcrawford.apod.ui.astronomypicture

import androidx.lifecycle.SavedStateHandle
import com.trevorcrawford.apod.data.fake.FakeAstronomyPictureRepository
import com.trevorcrawford.apod.data.fake.fakeAstronomyPictures
import com.trevorcrawford.apod.ui.MainDestinations
import com.trevorcrawford.apod.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AstronomyPictureDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AstronomyPictureDetailViewModel

    @Before
    fun setup() {
        val savedState =
            SavedStateHandle(mapOf(MainDestinations.APOD_DATE_KEY to fakeAstronomyPictures.first().date.toString()))

        viewModel = AstronomyPictureDetailViewModel(
            savedStateHandle = savedState,
            astronomyPictureRepository = FakeAstronomyPictureRepository()
        )
    }


    @Test
    fun viewmodel_get_picture_detail_returns_saved_state_handle_picture() = runTest {
        Assert.assertEquals(
            fakeAstronomyPictures.first(),
            (viewModel.uiState.first() as? AstronomyPictureDetailUiState.Data)?.astronomyPicture
        )
        Assert.assertEquals(
            fakeAstronomyPictures.first().explanation,
            (viewModel.uiState.first() as? AstronomyPictureDetailUiState.Data)?.astronomyPicture?.explanation
        )
    }
}