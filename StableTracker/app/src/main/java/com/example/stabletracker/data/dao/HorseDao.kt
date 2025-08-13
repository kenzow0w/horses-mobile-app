package com.example.stabletracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stabletracker.data.entities.Horse
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseDao {
    @Query("SELECT * FROM horses ORDER BY name ASC")
    fun getAllHorsesFlow(): Flow<List<Horse>>

    @Query("SELECT * FROM horses WHERE id = :horseId")
    fun getHorseFlow(horseId: Long): Flow<Horse?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(horse: Horse): Long

    @Query("DELETE FROM horses WHERE id = :horseId")
    suspend fun deleteById(horseId: Long)
}