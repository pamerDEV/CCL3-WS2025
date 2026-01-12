package com.example.mybuddy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.dao.HabitLogDao
import com.example.mybuddy.db.dao.MoodDao
import com.example.mybuddy.db.entity.HabitEntity
import com.example.mybuddy.db.entity.HabitLogEntity
import com.example.mybuddy.db.entity.MoodEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitLogEntity::class,
        MoodEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mybuddy_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}