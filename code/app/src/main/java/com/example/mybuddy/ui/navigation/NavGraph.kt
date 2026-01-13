package com.example.mybuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mybuddy.ui.screens.*

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Mood.route) {
            MoodScreen()
        }
        composable(Screen.Habits.route) {
            HabitScreen()
        }
        composable(Screen.Health.route) {
            HealthScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onCustomizeClick = {
                    navController.navigate(Screen.CustomizeBuddy.route)
                }
            )
        }
        composable(Screen.CustomizeBuddy.route) {
            CustomizeBuddyScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
