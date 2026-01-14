package com.example.mybuddy

import android.app.Application
import com.example.mybuddy.data.repository.BuddyRepository
import com.example.mybuddy.data.repository.HabitRepository
import com.example.mybuddy.data.repository.MoodRepository
import com.example.mybuddy.data.repository.SleepRepository
import com.example.mybuddy.data.repository.WaterRepository
import com.example.mybuddy.db.AppDatabase

class MyBuddyApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val buddyRepository by lazy { BuddyRepository(database.buddyProfileDao()) }
    val moodRepository by lazy { MoodRepository(database.moodDao()) }
    val habitRepository by lazy { HabitRepository(database.habitDao(), database.habitLogDao()) }
    val waterRepository by lazy { WaterRepository(database.waterLogDao()) }
    val sleepRepository by lazy { SleepRepository(database.sleepDao()) }
}