package com.trevorcrawford.apod.data

import java.time.LocalDate

data class AstronomyPicture(
    val title: String,
    val explanation: String,
    val date: LocalDate,
    val url: String,
    val hdUrl: String,
    val copyright: String
)
