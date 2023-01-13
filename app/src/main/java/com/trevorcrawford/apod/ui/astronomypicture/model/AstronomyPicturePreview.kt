package com.trevorcrawford.apod.ui.astronomypicture.model

import java.time.LocalDate

data class AstronomyPicturePreview(
    val title: String,
    val date: LocalDate,
    val thumbnailUrl: String,
    val copyright: String
)