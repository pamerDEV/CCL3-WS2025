package com.example.mybuddy.db.dao

import androidx.room.*
import com.example.mybuddy.db.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLog(log: HabitLogEntity)

    @Query("""
        SELECT * FROM habit_logs 
        WHERE habitId = :habitId AND date = :date
        LIMIT 1
    """)
    suspend fun getLogForDay(
        habitId: Long,
        date: String
    ): HabitLogEntity?

    @Query("""
        SELECT * FROM habit_logs 
        WHERE habitId = :habitId
        ORDER BY date DESC
    """)
    fun getLogsForHabit(
        habitId: Long
    ): Flow<List<HabitLogEntity>>

    @Query("""
        DELETE FROM habit_logs 
        WHERE habitId = :habitId
    """)
    suspend fun deleteLogsForHabit(habitId: Long)
}

