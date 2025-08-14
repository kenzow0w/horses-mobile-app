package com.example.stabletracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stabletracker.data.dao.HorseActivityDao
import com.example.stabletracker.data.dao.HorseDao
import com.example.stabletracker.data.entities.Horse
import com.example.stabletracker.data.entities.HorseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Horse::class, HorseActivity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StableDatabase : RoomDatabase() {
    abstract fun horseDao(): HorseDao
    abstract fun horseActivityDao(): HorseActivityDao

    companion object {
        @Volatile
        private var INSTANCE: StableDatabase? = null

        fun getInstance(context: Context): StableDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StableDatabase::class.java,
                    "stable_tracker_db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Prepopulate a few horses
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getInstance(context).horseDao()
                            dao.insert(Horse(name = "Болтун"))
                            dao.insert(Horse(name = "Крик"))
                            dao.insert(Horse(name = "Браслет"))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}