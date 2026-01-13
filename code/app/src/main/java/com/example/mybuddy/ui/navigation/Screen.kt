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
    object EditHabit : Screen("edit_habit", "Edit Habbit")

    // Profile Screens
    object CustomizeBuddy : Screen("customize_buddy", "Customize")

    companion object {
        val bottomItems = listOf(Home, Mood, Habits, Health, Profile)
    }
}
