package com.trevorcrawford.apod.ui.astronomypicture

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorcrawford.apod.R
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPictureUiState.*
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AstronomyPictureViewModel @Inject constructor(
    private val astronomyPictureRepository: AstronomyPictureRepository
) : ViewModel() {

    init {
        loadPictures()
    }

    /**
     * Sort option, limited to lifecycle of this viewmodel, but maintained across any uiState data
     * refresh (e.g. [loadPictures])
     */
    private var sortOption by mutableStateOf(availableSortOptions.first())

    val uiState: StateFlow<AstronomyPictureUiState> = combine(
        astronomyPictureRepository.astronomyPictures,
        snapshotFlow { sortOption }
    ) { astronomyPictureList, sortOption ->
        Data(
            previewList = astronomyPictureList.sortedBy { picture ->
                when (sortOption) {
                    PictureSortOption.TITLE -> picture.title
                    PictureSortOption.DATE -> picture.date.toString()
                }
            }.map {
                AstronomyPicturePreview(
                    title = it.title,
                    date = it.date,
                    thumbnailUrl = it.url
                )
            },
            sortOrderRes = sortOption.titleRes
        )
    }.catch {
        Timber.e(it.localizedMessage)
        Error(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun loadPictures() = viewModelScope.launch {
        astronomyPictureRepository.loadPictures()
            .onFailure {
                val message = it.localizedMessage
                Timber.e(message)
                //_errorMessage.update { message ?: "Something went wrong." }
            }
    }

    fun changeSortOption() {
        sortOption = availableSortOptions.first { it != sortOption }
    }

    companion object {
        /**
         * Available options for sorting the List<AstronomyPicture> displayed from uiState.
         */
        val availableSortOptions = listOf(PictureSortOption.TITLE, PictureSortOption.DATE)
    }
}

sealed class AstronomyPictureUiState {
    object Loading : AstronomyPictureUiState()

    data class Error(val throwable: Throwable) : AstronomyPictureUiState()

    data class Data(
        val previewList: List<AstronomyPicturePreview>,
        @StringRes val sortOrderRes: Int
    ) : AstronomyPictureUiState()
}

enum class PictureSortOption(
    @StringRes val titleRes: Int
) {
    TITLE(R.string.title),
    DATE(R.string.date);
}
