package com.example.stabletracker.data.repository

import com.example.stabletracker.data.dao.HorseActivityDao
import com.example.stabletracker.data.dao.HorseDao
import com.example.stabletracker.data.entities.Horse
import com.example.stabletracker.data.entities.HorseActivity
import kotlinx.coroutines.flow.Flow

class StableRepository(
    private val horseDao: HorseDao,
    private val horseActivityDao: HorseActivityDao
) {
    fun getAllHorses(): Flow<List<Horse>> = horseDao.getAllHorsesFlow()
    fun getHorse(horseId: Long): Flow<Horse?> = horseDao.getHorseFlow(horseId)
    suspend fun addHorse(name: String, breed: String?): Long =
        horseDao.insert(Horse(name = name, breed = breed))

    fun getActivitiesForHorse(horseId: Long): Flow<List<HorseActivity>> =
        horseActivityDao.getActivitiesForHorseFlow(horseId)

    suspend fun addActivity(activity: HorseActivity): Long =
        horseActivityDao.insert(activity)
}