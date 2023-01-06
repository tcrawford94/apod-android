package com.trevorcrawford.apod.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class AstronomyPicture(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface AstronomyPictureDao {
    @Query("SELECT * FROM astronomypicture ORDER BY uid DESC LIMIT 10")
    fun getAstronomyPictures(): Flow<List<AstronomyPicture>>

    @Insert
    suspend fun insertAstronomyPicture(item: AstronomyPicture)
}
