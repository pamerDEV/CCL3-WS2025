package com.example.mybuddy.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 0,

    val waterGoalMl: Int = 2000,
    val sleepGoalMinutes: Int = 8 * 60,
    val onboardingDone: Boolean = false
)
