package com.trevorcrawford.apod.ui.astronomypicture

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorcrawford.apod.R
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPicturesUiState.*
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AstronomyPicturesViewModel @Inject constructor(
    private val astronomyPictureRepository: AstronomyPictureRepository
) : ViewModel() {

    init {
        loadPictures()
    }

    /**
     * Sort option, limited to lifecycle of this viewmodel, but maintained across any uiState data
     * refresh (e.g. [loadPictures])
     */
    private var sortOption = MutableStateFlow(availableSortOptions.first())

    val uiState: StateFlow<AstronomyPicturesUiState> = combine(
        astronomyPictureRepository.astronomyPictures,
        sortOption,
        astronomyPictureRepository.isRefreshingPictures
    ) { astronomyPictureList, sortOption, isRefreshing ->
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
            sortOrderRes = sortOption.titleRes,
            isRefreshing = isRefreshing
        )
    }.catch {
        Timber.e(it.localizedMessage)
        Error(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Loading
    )

    fun loadPictures() = viewModelScope.launch {
        astronomyPictureRepository.loadPictures()
            .onFailure {
                val message = it.localizedMessage
                Timber.e(message)
                //_errorMessage.update { message ?: "Something went wrong." }
            }
    }

    fun changeSortOption() {
        sortOption.update { currentOption ->
            when (currentOption) {
                PictureSortOption.TITLE -> PictureSortOption.DATE
                PictureSortOption.DATE -> PictureSortOption.TITLE
            }
        }
    }

    companion object {
        /**
         * Available options for sorting the List<AstronomyPicture> displayed from uiState.
         */
        val availableSortOptions = listOf(PictureSortOption.TITLE, PictureSortOption.DATE)
    }
}

sealed interface AstronomyPicturesUiState {
    object Loading : AstronomyPicturesUiState

    data class Error(val throwable: Throwable) : AstronomyPicturesUiState

    data class Data(
        val previewList: List<AstronomyPicturePreview>,
        @StringRes val sortOrderRes: Int,
        val isRefreshing: Boolean
    ) : AstronomyPicturesUiState
}

enum class PictureSortOption(
    @StringRes val titleRes: Int
) {
    TITLE(R.string.title),
    DATE(R.string.date);
}
