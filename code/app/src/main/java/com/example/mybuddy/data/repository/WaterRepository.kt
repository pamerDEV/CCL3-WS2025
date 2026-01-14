package com.example.mybuddy.data.repository

import com.example.mybuddy.db.dao.WaterLogDao
import com.example.mybuddy.db.entity.WaterLogEntity
import kotlinx.coroutines.flow.Flow

class WaterRepository(private val waterLogDao: WaterLogDao) {

    fun getLast7Days(): Flow<List<WaterLogEntity>> = waterLogDao.getLast7Days()

    fun getAll(): Flow<List<WaterLogEntity>> = waterLogDao.getAll()

    suspend fun getForDate(date: String): WaterLogEntity? {
        return waterLogDao.getForDate(date)
    }

    suspend fun insert(log: WaterLogEntity) {
        waterLogDao.insert(log)
    }

    suspend fun update(log: WaterLogEntity) {
        waterLogDao.update(log)
    }
}