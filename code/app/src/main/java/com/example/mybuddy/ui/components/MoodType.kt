package com.example.mybuddy.ui.components

import com.example.mybuddy.R

enum class MoodType(
    val label: String,
    val iconRes: Int,
    val points: Int
) {
    AWESOME("Awesome", R.drawable.mood_awesome, 30),
    LOVED("Loved", R.drawable.mood_loved, 28),
    OKAY("Okay", R.drawable.mood_okay, 20),
    TIRED("Tired", R.drawable.mood_tired, 12),
    ANXIOUS("Anxious", R.drawable.mood_anxious, 10),
    SAD("Sad", R.drawable.mood_sad, 8),
    UPSET("Upset", R.drawable.mood_upset, 5),
    TERRIBLE("Terrible", R.drawable.mood_terrible, 3);

    companion object {
        fun fromString(value: String): MoodType {
            return entries.find { it.name == value } ?: OKAY
        }
    }
}