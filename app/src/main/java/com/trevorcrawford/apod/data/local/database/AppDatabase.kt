package com.trevorcrawford.apod.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.trevorcrawford.apod.data.local.database.model.RoomAstronomyPicture

@Database(
    version = 2,
    entities = [RoomAstronomyPicture::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun astronomyPictureDao(): AstronomyPictureDao
}
