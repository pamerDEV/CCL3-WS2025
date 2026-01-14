package com.example.mybuddy.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_logs")
data class SleepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,           // "2026-01-15"
    val bedtime: String,        // "23:59"
    val wakeTime: String,       // "06:00"
    val quality: String,        // "TERRIBLE", "OKAY", "AWESOME"
    val durationMinutes: Int,   // Berechnete Dauer in Minuten
    val goalMinutes: Int = 480  // Default 8 Stunden
)