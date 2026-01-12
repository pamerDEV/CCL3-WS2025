package com.example.mybuddy.db.dao

import androidx.room.*
import com.example.mybuddy.db.entity.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Insert
    suspend fun insertMood(mood: MoodEntity): Long

    @Update
    suspend fun updateMood(mood: MoodEntity)

    @Delete
    suspend fun deleteMood(mood: MoodEntity)

    @Query("SELECT * FROM moods ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM moods WHERE id = :id LIMIT 1")
    suspend fun getMoodById(id: Long): MoodEntity?

    @Query("SELECT * FROM moods WHERE timestamp >= :startOfDay AND timestamp < :endOfDay LIMIT 1")
    suspend fun getMoodByDate(startOfDay: Long, endOfDay: Long): MoodEntity?

    @Query("DELETE FROM moods WHERE id = :id")
    suspend fun deleteMoodById(id: Long)
}
