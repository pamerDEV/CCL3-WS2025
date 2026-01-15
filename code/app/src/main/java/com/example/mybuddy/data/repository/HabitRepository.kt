package com.example.mybuddy.data.repository

import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.dao.HabitLogDao
import com.example.mybuddy.db.entity.HabitEntity
import com.example.mybuddy.db.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

class HabitRepository(
    private val habitDao: HabitDao,
    private val habitLogDao: HabitLogDao
) {

    val allLogs: Flow<List<HabitLogEntity>> = habitLogDao.getAllLogs()

    fun getAllHabits(): Flow<List<HabitEntity>> = habitDao.getAllHabits()

    fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>> =
        habitLogDao.getLogsForHabit(habitId)

    suspend fun getHabitById(id: Long): HabitEntity? {
        return habitDao.getHabitById(id)
    }

    suspend fun insertHabit(habit: HabitEntity): Long {
        return habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit)
    }

    suspend fun upsertLog(log: HabitLogEntity) {
        habitLogDao.upsertLog(log)
    }

    suspend fun getLogForDate(habitId: Long, date: String): HabitLogEntity? {
        return habitLogDao.getLogForDay(habitId, date)
    }

    suspend fun deleteLogsForHabit(habitId: Long) {
        habitLogDao.deleteLogsForHabit(habitId)
    }
}