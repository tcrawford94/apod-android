package com.trevorcrawford.apod.data.local.database

import androidx.room.*
import com.trevorcrawford.apod.data.local.database.model.RoomAstronomyPicture
import kotlinx.coroutines.flow.Flow

@Dao
interface AstronomyPictureDao {
    @Query("SELECT * FROM astronomy_pictures")
    fun getAstronomyPictures(): Flow<List<RoomAstronomyPicture>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictures: List<RoomAstronomyPicture>)

    @Query("DELETE FROM astronomy_pictures")
    suspend fun clear()
}
