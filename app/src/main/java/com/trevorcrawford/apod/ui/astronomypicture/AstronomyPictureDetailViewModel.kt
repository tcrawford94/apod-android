package com.trevorcrawford.apod.ui.astronomypicture

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorcrawford.apod.data.AstronomyPicture
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.ui.MainDestinations.APOD_DATE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AstronomyPictureDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val astronomyPictureRepository: AstronomyPictureRepository,
) : ViewModel() {
    val uiState: StateFlow<AstronomyPictureDetailUiState> =
        savedStateHandle.getStateFlow<String?>(APOD_DATE_KEY, null)
            .flatMapLatest {
                astronomyPictureRepository.getPictureDetail(LocalDate.parse(it))
            }
            .mapNotNull {
                if (it == null) {
                    AstronomyPictureDetailUiState.Loading
                } else {
                    AstronomyPictureDetailUiState.Data(it)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AstronomyPictureDetailUiState.Loading
            )
}

sealed interface AstronomyPictureDetailUiState {
    object Loading : AstronomyPictureDetailUiState
    data class Error(val throwable: Throwable) : AstronomyPictureDetailUiState
    data class Data(val astronomyPicture: AstronomyPicture) : AstronomyPictureDetailUiState
}