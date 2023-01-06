package com.trevorcrawford.apod.ui.astronomypicture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorcrawford.apod.data.AstronomyPictureRepository
import com.trevorcrawford.apod.ui.astronomypicture.AstronomyPictureUiState.*
import com.trevorcrawford.apod.ui.astronomypicture.model.AstronomyPicturePreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AstronomyPictureViewModel @Inject constructor(
    private val astronomyPictureRepository: AstronomyPictureRepository
) : ViewModel() {

    val uiState: StateFlow<AstronomyPictureUiState> = combine(
        astronomyPictureRepository.astronomyPictures
    ) {
        Success(data = listOf())
    }.catch {
        Error(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addAstronomyPicture(name: String) {
        viewModelScope.launch {
            astronomyPictureRepository.add(name)
        }
    }
}

sealed interface AstronomyPictureUiState {
    object Loading : AstronomyPictureUiState
    data class Error(val throwable: Throwable) : AstronomyPictureUiState
    data class Success(val data: List<AstronomyPicturePreview>) : AstronomyPictureUiState
}
