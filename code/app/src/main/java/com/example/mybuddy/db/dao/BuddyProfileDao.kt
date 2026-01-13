package com.example.mybuddy.db.dao

import androidx.room.*
import com.example.mybuddy.db.entity.BuddyProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuddyProfileDao {

    @Query("SELECT * FROM buddy_profile WHERE id = 1")
    fun getBuddyProfile(): Flow<BuddyProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: BuddyProfileEntity)

    @Query("UPDATE buddy_profile SET name = :name WHERE id = 1")
    suspend fun updateName(name: String)

    @Query("UPDATE buddy_profile SET colorHex = :colorHex WHERE id = 1")
    suspend fun updateColor(colorHex: String)
}