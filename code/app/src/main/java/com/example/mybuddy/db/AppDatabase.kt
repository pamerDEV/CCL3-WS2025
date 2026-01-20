package com.example.mybuddy.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mybuddy.db.dao.BuddyProfileDao
import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.dao.HabitLogDao
import com.example.mybuddy.db.dao.MoodDao
import com.example.mybuddy.db.dao.SleepDao
import com.example.mybuddy.db.dao.UserSettingsDao
import com.example.mybuddy.db.dao.WaterLogDao
import com.example.mybuddy.db.entity.BuddyProfileEntity
import com.example.mybuddy.db.entity.HabitEntity
import com.example.mybuddy.db.entity.HabitLogEntity
import com.example.mybuddy.db.entity.MoodEntity
import com.example.mybuddy.db.entity.SleepEntity
import com.example.mybuddy.db.entity.UserSettingsEntity
import com.example.mybuddy.db.entity.WaterLogEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitLogEntity::class,
        MoodEntity::class,
        BuddyProfileEntity::class,
        WaterLogEntity::class,
        SleepEntity::class,
        UserSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun moodDao(): MoodDao
    abstract fun buddyProfileDao(): BuddyProfileDao
    abstract fun waterLogDao(): WaterLogDao
    abstract fun sleepDao(): SleepDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mybuddy_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}