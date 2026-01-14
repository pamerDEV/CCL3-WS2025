package com.example.mybuddy.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mybuddy.db.entity.SleepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {

    @Query("SELECT * FROM sleep_logs ORDER BY date DESC")
    fun getAllSleepLogs(): Flow<List<SleepEntity>>

    @Query("SELECT * FROM sleep_logs WHERE date = :date LIMIT 1")
    suspend fun getSleepByDate(date: String): SleepEntity?

    @Query("SELECT * FROM sleep_logs WHERE id = :id")
    suspend fun getSleepById(id: Long): SleepEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleep(sleep: SleepEntity): Long

    @Update
    suspend fun updateSleep(sleep: SleepEntity)

    @Delete
    suspend fun deleteSleep(sleep: SleepEntity)
}