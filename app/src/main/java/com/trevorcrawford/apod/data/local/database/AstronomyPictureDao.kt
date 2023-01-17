package com.trevorcrawford.apod.data.local.database

import androidx.room.*
import com.trevorcrawford.apod.data.local.database.model.RoomAstronomyPicture
import kotlinx.coroutines.flow.Flow

@Dao
interface AstronomyPictureDao {
    @Query("SELECT * FROM ${RoomAstronomyPicture.TABLE_NAME}")
    fun getAstronomyPictures(): Flow<List<RoomAstronomyPicture>>

    @Query("SELECT * FROM ${RoomAstronomyPicture.TABLE_NAME} WHERE date = :date")
    fun getAstronomyPicture(date: String): Flow<RoomAstronomyPicture>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictures: List<RoomAstronomyPicture>)

    @Query("DELETE FROM ${RoomAstronomyPicture.TABLE_NAME}")
    suspend fun clear()
}
