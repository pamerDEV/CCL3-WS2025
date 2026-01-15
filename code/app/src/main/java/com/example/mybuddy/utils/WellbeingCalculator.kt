package com.example.mybuddy.utils

import com.example.mybuddy.ui.components.BlobMood
import com.example.mybuddy.ui.components.MoodType

object WellbeingCalculator {

    /**
     * Calculates Wellbeing Score (0-100) from mood, habits, sleep, and water data
     */
    fun calculateScore(
        todayMood: MoodType?,
        habitsCompleted: Int,
        totalHabits: Int,
        sleepDurationMinutes: Int?,
        sleepGoalMinutes: Int,
        sleepQuality: String?,
        waterIntakeMl: Int,
        waterGoalMl: Int
    ): Int {
        var score = 0

        // Mood Points (0-30)
        score += getMoodPoints(todayMood)

        // Habits Points (0-30)
        score += getHabitsPoints(habitsCompleted, totalHabits)

        // Sleep Points (0-25)
        score += getSleepPoints(sleepDurationMinutes, sleepGoalMinutes, sleepQuality)

        // Water Points (0-15)
        score += getWaterPoints(waterIntakeMl, waterGoalMl)

        return score.coerceIn(0, 100)
    }

    /**
     * Mood Points (0-30)
     * Uses points from MoodType enum
     */
    private fun getMoodPoints(mood: MoodType?): Int {
        return mood?.points ?: 15 // Default middle value if no mood logged
    }

    /**
     * Habits Points (0-30)
     * Formula: (completed / total) * 30
     */
    private fun getHabitsPoints(completed: Int, total: Int): Int {
        if (total == 0) return 15 // Default if no habits created
        return ((completed.toFloat() / total) * 30).toInt().coerceIn(0, 30)
    }

    /**
     * Sleep Points (0-25)
     * Duration: (actual / goal) * 15, max 15
     * Quality: Awesome=10, Okay=6, Terrible=2
     */
    private fun getSleepPoints(durationMinutes: Int?, goalMinutes: Int, quality: String?): Int {
        if (durationMinutes == null) return 12 // Default if no sleep logged

        // Duration points (0-15)
        val durationPoints = ((durationMinutes.toFloat() / goalMinutes) * 15)
            .toInt()
            .coerceIn(0, 15)

        // Quality points (0-10)
        val qualityPoints = when (quality?.uppercase()) {
            "AWESOME" -> 10
            "OKAY" -> 6
            "TERRIBLE" -> 2
            else -> 6
        }

        return (durationPoints + qualityPoints).coerceIn(0, 25)
    }

    /**
     * Water Points (0-15)
     * Formula: (intake / goal) * 15
     */
    private fun getWaterPoints(intakeMl: Int, goalMl: Int): Int {
        if (goalMl == 0) return 7 // Default
        return ((intakeMl.toFloat() / goalMl) * 15).toInt().coerceIn(0, 15)
    }

    /**
     * Maps Wellbeing Score to Buddy Mood
     */
    fun scoreToBuddyMood(score: Int): BlobMood {
        return when {
            score >= 85 -> BlobMood.EXCITED
            score >= 65 -> BlobMood.HAPPY
            score >= 45 -> BlobMood.WORRIED
            score >= 25 -> BlobMood.SAD
            else -> BlobMood.SLEEPY
        }
    }
}