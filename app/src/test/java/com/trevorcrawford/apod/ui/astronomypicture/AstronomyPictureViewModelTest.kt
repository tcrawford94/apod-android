package com.trevorcrawford.apod.ui.astronomypicture


import com.trevorcrawford.apod.data.AstronomyPictureRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class AstronomyPictureViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = AstronomyPictureViewModel(FakeAstronomyPictureRepository())
        assertEquals(viewModel.uiState.first(), AstronomyPictureUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = AstronomyPictureViewModel(FakeAstronomyPictureRepository())
        assertEquals(viewModel.uiState.first(), AstronomyPictureUiState.Loading)
    }
}

private class FakeAstronomyPictureRepository : AstronomyPictureRepository {

    private val data = mutableListOf<String>()

    override val astronomyPictures: Flow<List<String>>
        get() = flow { emit(data.toList()) }

    override suspend fun add(name: String) {
        data.add(0, name)
    }
}
