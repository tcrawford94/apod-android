package com.trevorcrawford.apod.data.remote.retrofit.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@Keep
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
    val isPicture = mediaType == IMAGE_MEDIA_TYPE

    companion object {
        const val IMAGE_MEDIA_TYPE = "image"
    }
}