package com.example.stabletracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horses")
data class Horse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val breed: String? = null,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)