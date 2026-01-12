package com.example.mybuddy.db.dao

import androidx.room.*
import com.example.mybuddy.db.entity.HabitEntity

@Dao
interface HabitDao {

    @Insert
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Long): HabitEntity?
}