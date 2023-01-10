package com.trevorcrawford.apod.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trevorcrawford.apod.data.local.database.model.RoomAstronomyPicture

@Database(entities = [RoomAstronomyPicture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun astronomyPictureDao(): AstronomyPictureDao
}
