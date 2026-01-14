package com.example.mybuddy.db.dao

import androidx.room.*
import com.example.mybuddy.db.entity.WaterLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: WaterLogEntity)

    @Update
    suspend fun update(log: WaterLogEntity)

    @Query("""
        SELECT * FROM water_logs
        WHERE date = :date
        LIMIT 1
    """)
    suspend fun getForDate(date: String): WaterLogEntity?

    @Query("""
        SELECT * FROM water_logs
        ORDER BY date DESC
        LIMIT 7
    """)
    fun getLast7Days(): Flow<List<WaterLogEntity>>

    @Query("""
        SELECT * FROM water_logs
        ORDER BY date DESC
    """)
    fun getAll(): Flow<List<WaterLogEntity>>
}
