package com.example.mybuddy.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buddy_profile")
data class BuddyProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Buddy",
    val colorHex: String = "#9756F2"  // Default Violet
)