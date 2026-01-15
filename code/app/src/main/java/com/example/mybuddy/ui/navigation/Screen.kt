package com.example.mybuddy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mybuddy.R

sealed class Screen(
    val route: String,
    val label: String,
    val iconRes: Int = 0
) {
    // Navbar Screens
    object Home : Screen("home", "Home", R.drawable.home_icon)
    object Mood : Screen("mood", "Mood", R.drawable.mood_icon)
    object Habits : Screen("habits", "Habits", R.drawable.habits_icon)
    object Health : Screen("health", "Health", R.drawable.health_icon)
    object Profile : Screen("profile", "Profile", R.drawable.profile_icon)

    // Habit Screens
    object AddHabit : Screen("add_habit", "Create new Habit")
    object EditHabit : Screen("edit_habit", "Edit Habit")

    // Profile Screen
    object CustomizeBuddy : Screen("customize_buddy", "Customize")

    //Mood Screens
    object AddMood : Screen("add_mood?timestamp={timestamp}", "Add Mood") {
        fun createRoute(timestamp: Long? = null): String {
            return if (timestamp != null) {
                "add_mood?timestamp=$timestamp"
            } else {
                "add_mood"
            }
        }
    }
    object MoodDayView : Screen("mood_day/{timestamp}", "Mood Day") {
        fun createRoute(timestamp: Long) = "mood_day/$timestamp"
    }
    object EditMood : Screen("edit_mood/{moodId}", "Edit Mood") {
        fun createRoute(moodId: Long) = "edit_mood/$moodId"
    }

    //Health Sleep
    object AddSleep : Screen("add_sleep", "Add Sleep")


    companion object {
        val bottomItems = listOf(Home, Mood, Habits, Health, Profile)

        val bottomRoutes = bottomItems.map { it.route }
    }
}
