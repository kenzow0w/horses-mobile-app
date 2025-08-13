package com.example.stabletracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stabletracker.data.entities.ActivityType
import com.example.stabletracker.data.entities.Horse
import com.example.stabletracker.data.entities.HorseActivity
import com.example.stabletracker.data.repository.StableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HorseDetailViewModel(
    private val horseId: Long,
    private val repository: StableRepository
) : ViewModel() {

    val horse: StateFlow<Horse?> = repository
        .getHorse(horseId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val activities: Flow<List<HorseActivity>> = repository
        .getActivitiesForHorse(horseId)

    fun addActivity(
        type: ActivityType,
        timestampEpochMillis: Long,
        durationMinutes: Int?,
        notes: String?
    ) {
        viewModelScope.launch {
            repository.addActivity(
                HorseActivity(
                    horseId = horseId,
                    type = type,
                    timestampEpochMillis = timestampEpochMillis,
                    durationMinutes = durationMinutes,
                    notes = notes
                )
            )
        }
    }
}