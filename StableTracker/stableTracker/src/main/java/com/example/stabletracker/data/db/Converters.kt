package com.example.stabletracker.data.db

import androidx.room.TypeConverter
import com.example.stabletracker.data.entities.ActivityType

class Converters {
    @TypeConverter
    fun toActivityType(value: String): ActivityType = ActivityType.valueOf(value)

    @TypeConverter
    fun fromActivityType(type: ActivityType): String = type.name
}