package com.example.stabletracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stabletracker.data.repository.StableRepository
import com.example.stabletracker.data.entities.Horse
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HorseListViewModel(
    private val repository: StableRepository
) : ViewModel() {

    val horses: StateFlow<List<Horse>> = repository
        .getAllHorses()
        .map { list -> list.sortedBy { it.name.lowercase() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addHorse(name: String, breed: String?) {
        viewModelScope.launch {
            repository.addHorse(name, breed)
        }
    }
}