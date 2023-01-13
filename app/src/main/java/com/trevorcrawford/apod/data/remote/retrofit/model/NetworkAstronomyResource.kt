package com.trevorcrawford.apod.data.remote.retrofit.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class NetworkAstronomyResource(
    val title: String? = "",
    val explanation: String? = "",
    val date: LocalDate? = null,
    @Json(name = "media_type") val mediaType: String? = "",
    @Json(name = "hdurl") val hdUrl: String? = "",
    val url: String? = "",
    val copyright: String? = ""
) {
    fun isPicture() = mediaType == MediaType.IMAGE.type

    enum class MediaType(val type: String) {
        IMAGE("image")
    }
}