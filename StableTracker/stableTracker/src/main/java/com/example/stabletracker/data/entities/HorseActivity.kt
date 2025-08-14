package com.example.stabletracker.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "horse_activities",
    foreignKeys = [
        ForeignKey(
            entity = Horse::class,
            parentColumns = ["id"],
            childColumns = ["horseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["horseId"])],
)
data class HorseActivity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val horseId: Long,
    val type: ActivityType,
    val timestampEpochMillis: Long = System.currentTimeMillis(),
    val durationMinutes: Int? = null,
    val notes: String? = null,
)