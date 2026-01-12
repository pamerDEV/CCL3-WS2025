package com.example.mybuddy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.dao.HabitLogDao
import com.example.mybuddy.db.entity.HabitEntity
import com.example.mybuddy.db.entity.HabitLogEntity

@Database(
    entities = [HabitEntity::class, HabitLogEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
}
