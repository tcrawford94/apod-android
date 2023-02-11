package com.trevorcrawford.apod.ui.astronomypicture

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorcrawford.apod.R
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPicturesUiState.Data
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPicturesUiState.Error
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import com.trevorcrawford.apod.ui.util.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class AstronomyPicturesViewModel @Inject constructor(
    private val astronomyPictureRepository: AstronomyPictureRepository,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    /**
     * Sort option, limited to lifecycle of this viewmodel, but maintained across any uiState data
     * refresh (e.g. [loadPictures])
     */
    private val sortOption = MutableStateFlow(availableSortOptions.first())
    private val isLoadingPictures = MutableStateFlow(false)

    val uiState: StateFlow<AstronomyPicturesUiState> = combine(
        astronomyPictureRepository.astronomyPictures.onEach {
            if (it.isEmpty()) {
                loadPictures()
                //isLoadingPictures.update { true }
            }
        },
        sortOption,
        isLoadingPictures
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
                    thumbnailUrl = it.url,
                    copyright = it.copyright
                )
            }.toImmutableList(),
            sortOrderRes = sortOption.titleRes,
            isRefreshing = isRefreshing
        )
    }.catch {
        Timber.e(it.localizedMessage)
        snackbarManager.showMessage(
            when (it) {
                is UnknownHostException -> R.string.error_no_network
                else -> R.string.error_unable_to_load_pictures
            }
        )
        Error(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        // initialValue UiState has isRefreshing true b/c waiting for first value from repository
        initialValue = Data(persistentListOf(), sortOption.value.titleRes, false)
    )

    fun loadPictures() = viewModelScope.launch {
        if (isLoadingPictures.value) return@launch
        isLoadingPictures.update { true }
        astronomyPictureRepository.loadPictures()
            .onSuccess {
                isLoadingPictures.update { false }
            }
            .onFailure {
                isLoadingPictures.update { false }
                Timber.e(it.localizedMessage)
                snackbarManager.showMessage(
                    when (it) {
                        is UnknownHostException -> R.string.error_no_network
                        else -> R.string.error_unable_to_load_pictures
                    }
                )
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
    data class Data(
        val previewList: ImmutableList<AstronomyPicturePreview>,
        @StringRes val sortOrderRes: Int,
        val isRefreshing: Boolean
    ) : AstronomyPicturesUiState

    data class Error(val throwable: Throwable) : AstronomyPicturesUiState
}

enum class PictureSortOption(
    @StringRes val titleRes: Int
) {
    TITLE(R.string.title),
    DATE(R.string.date);
}
