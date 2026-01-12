package com.example.mybuddy.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moods")
data class MoodEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,

        val moodType: String,
        val note: String? = null,
        val timestamp: Long = System.currentTimeMillis()
)