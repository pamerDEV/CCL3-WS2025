package com.example.mybuddy.data.repository

import com.example.mybuddy.db.dao.SleepDao
import com.example.mybuddy.db.entity.SleepEntity
import kotlinx.coroutines.flow.Flow

class SleepRepository(private val sleepDao: SleepDao) {
    val allLogs : Flow<List<SleepEntity>> = sleepDao.getAllSleepLogs()

    fun getAllSleepLogs(): Flow<List<SleepEntity>> = sleepDao.getAllSleepLogs()

    suspend fun getSleepByDate(date: String): SleepEntity? {
        return sleepDao.getSleepByDate(date)
    }

    suspend fun getSleepById(id: Long): SleepEntity? {
        return sleepDao.getSleepById(id)
    }

    suspend fun insertSleep(sleep: SleepEntity): Long {
        return sleepDao.insertSleep(sleep)
    }

    suspend fun updateSleep(sleep: SleepEntity) {
        sleepDao.updateSleep(sleep)
    }

    suspend fun deleteSleep(sleep: SleepEntity) {
        sleepDao.deleteSleep(sleep)
    }
}