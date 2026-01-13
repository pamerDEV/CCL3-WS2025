package com.example.mybuddy

import android.app.Application
import com.example.mybuddy.data.repository.BuddyRepository
import com.example.mybuddy.db.AppDatabase

class MyBuddyApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val buddyRepository by lazy { BuddyRepository(database.buddyProfileDao()) }
}