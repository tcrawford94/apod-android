package com.trevorcrawford.apod.data.remote.retrofit.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate


class DayAdapter {
    @ToJson
    fun toJson(date: LocalDate): String = date.toString()

    /**
     * Maps ISO-8601 Date String to a [LocalDate]
     */
    @FromJson
    fun fromJson(date: String): LocalDate = LocalDate.parse(date)
}