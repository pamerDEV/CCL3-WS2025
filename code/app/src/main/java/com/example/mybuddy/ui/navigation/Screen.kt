package com.example.mybuddy.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mybuddy.R

sealed class Screen(
    val route: String,
    val label: String,
    val iconRes: Int
) {
    object Home : Screen("home", "Home", R.drawable.home_icon)
    object Mood : Screen("mood", "Mood", R.drawable.mood_icon)
    object Habits : Screen("habits", "Habits", R.drawable.habits_icon)
    object Health : Screen("health", "Health", R.drawable.health_icon)
    object Profile : Screen("profile", "Profile", R.drawable.profile_icon)

    companion object {
        val bottomItems = listOf(Home, Mood, Habits, Health, Profile)
    }
}
