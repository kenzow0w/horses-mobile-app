package com.example.stabletracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stabletracker.data.entities.HorseActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface HorseActivityDao {
    @Query("SELECT * FROM horse_activities WHERE horseId = :horseId ORDER BY timestampEpochMillis DESC")
    fun getActivitiesForHorseFlow(horseId: Long): Flow<List<HorseActivity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: HorseActivity): Long

    @Query("DELETE FROM horse_activities WHERE id = :activityId")
    suspend fun deleteById(activityId: Long)
}