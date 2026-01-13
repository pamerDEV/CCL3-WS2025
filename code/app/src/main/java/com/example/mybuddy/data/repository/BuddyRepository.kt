package com.example.mybuddy.data.repository

import com.example.mybuddy.db.dao.BuddyProfileDao
import com.example.mybuddy.db.entity.BuddyProfileEntity
import kotlinx.coroutines.flow.Flow

class BuddyRepository(private val buddyProfileDao: BuddyProfileDao) {

    val buddyProfile: Flow<BuddyProfileEntity?> = buddyProfileDao.getBuddyProfile()

    suspend fun saveBuddyProfile(name: String, colorHex: String) {
        buddyProfileDao.insertOrUpdate(
            BuddyProfileEntity(
                id = 1,
                name = name,
                colorHex = colorHex
            )
        )
    }

    suspend fun updateName(name: String) {
        buddyProfileDao.updateName(name)
    }

    suspend fun updateColor(colorHex: String) {
        buddyProfileDao.updateColor(colorHex)
    }
}