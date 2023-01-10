package com.trevorcrawford.apod.data.local.database

import androidx.room.TypeConverter
import java.time.LocalDate

class TypeConverters {
    object LocalDateTypeConverter {
        @TypeConverter
        fun fromLocalDate(date: LocalDate): String = date.toString()

        @TypeConverter
        fun toLocalDate(date: String): LocalDate = LocalDate.parse(date)
    }
}