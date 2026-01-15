package com.example.mybuddy.ui.components

import com.example.mybuddy.R

enum class MoodType(
    val label: String,
    val iconRes: Int,
    val points: Int,
    val isPositive: Boolean
) {
    AWESOME("Awesome", R.drawable.mood_awesome, 30, true),
    LOVED("Loved", R.drawable.mood_loved, 28, true),
    OKAY("Okay", R.drawable.mood_okay, 20, true),
    TIRED("Tired", R.drawable.mood_tired, 12, false),
    ANXIOUS("Anxious", R.drawable.mood_anxious, 10, false),
    SAD("Sad", R.drawable.mood_sad, 8, false),
    UPSET("Upset", R.drawable.mood_upset, 5, false),
    TERRIBLE("Terrible", R.drawable.mood_terrible, 3, false);

    companion object {
        fun fromString(value: String): MoodType {
            return entries.find { it.name == value } ?: OKAY
        }
    }
}