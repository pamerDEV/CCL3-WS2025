package com.example.mybuddy.data.repository

import com.example.mybuddy.db.dao.MoodDao
import com.example.mybuddy.db.entity.MoodEntity
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val moodDao: MoodDao) {

    val allMoods: Flow<List<MoodEntity>> = moodDao.getAllMoods()

    suspend fun insertMood(mood: MoodEntity): Long {
        return moodDao.insertMood(mood)
    }

    suspend fun updateMood(mood: MoodEntity) {
        moodDao.updateMood(mood)
    }

    suspend fun deleteMood(mood: MoodEntity) {
        moodDao.deleteMood(mood)
    }

    suspend fun getMoodById(id: Long): MoodEntity? {
        return moodDao.getMoodById(id)
    }

    suspend fun getMoodByDate(startOfDay: Long, endOfDay: Long): MoodEntity? {
        return moodDao.getMoodByDate(startOfDay, endOfDay)
    }
}