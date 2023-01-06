package com.trevorcrawford.apod.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AstronomyPicture::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun astronomyPictureDao(): AstronomyPictureDao
}
