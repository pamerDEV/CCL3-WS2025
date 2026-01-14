package com.example.mybuddy.ui.components

import com.example.mybuddy.R

enum class SleepQuality(
    val label: String,
    val iconRes: Int,
    val points: Int
) {
    TERRIBLE("Terrible", R.drawable.mood_terrible, 5),
    OKAY("Okay", R.drawable.mood_okay, 15),
    AWESOME("Awesome", R.drawable.mood_awesome, 25);

    companion object {
        fun fromString(value: String): SleepQuality {
            return entries.find { it.name == value } ?: OKAY
        }
    }
}