package com.trevorcrawford.apod.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.trevorcrawford.apod.data.AstronomyPicture
import com.trevorcrawford.apod.data.local.database.TypeConverters.LocalDateTypeConverter
import java.time.LocalDate

@TypeConverters(LocalDateTypeConverter::class)
@Entity(tableName = "astronomy_pictures")
data class RoomAstronomyPicture(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val title: String,
    val explanation: String,
    @ColumnInfo(index = true) val date: LocalDate,
    val url: String,
    val hdUrl: String,
    @ColumnInfo(defaultValue = "") val copyright: String
)

fun RoomAstronomyPicture.asExternalModel() = AstronomyPicture(
    title = title,
    explanation = explanation,
    date = date,
    url = url,
    hdUrl = hdUrl,
    copyright = copyright
)