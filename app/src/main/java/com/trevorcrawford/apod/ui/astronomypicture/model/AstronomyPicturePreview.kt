package com.trevorcrawford.apod.ui.astronomypicture.model

import java.time.format.DateTimeFormatter

data class AstronomyPicturePreview (
    val title: String,
    val subTitle: String,
    val thumbnailUrl: String
) {
    companion object {
        private val monthDayYearFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    }
}