package com.example.mybuddy.data.repository

import com.example.mybuddy.db.dao.UserSettingsDao
import com.example.mybuddy.db.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserSettingsRepository(
    private val dao: UserSettingsDao
) {

    val settings: Flow<UserSettingsEntity> =
        dao.observe()
            .map { it ?: UserSettingsEntity() }

    suspend fun updateWaterGoal(goal: Int) {
        val current = dao.observe().first() ?: UserSettingsEntity()
        dao.upsert(current.copy(waterGoalMl = goal))
    }

    suspend fun updateSleepGoal(minutes: Int) {
        val current = dao.observe().first() ?: UserSettingsEntity()
        dao.upsert(current.copy(sleepGoalMinutes = minutes))
    }

    suspend fun setOnboardingDone() {
        val current = dao.observe().first() ?: UserSettingsEntity()
        dao.upsert(current.copy(onboardingDone = true))
    }
}
