package com.example.mybuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mybuddy.ui.screens.*
import com.example.mybuddy.ui.screens.habit.AddHabitScreen
import com.example.mybuddy.ui.screens.habit.EditHabitScreen
import com.example.mybuddy.ui.screens.habit.HabitScreen

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
            HabitScreen(
                onCreateHabit = {
                    navController.navigate(Screen.AddHabit.route)
                },
                navController
            )
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
        /* HABIT SCREENS */
        composable(Screen.AddHabit.route) {
            AddHabitScreen(
                onBack = {
                    navController.popBackStack()
                },
                onHabitCreated = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.EditHabit.route + "/{habitId}",
            arguments = listOf(
                navArgument("habitId") { type = NavType.LongType}
            )
        ) { backstackEntry ->
            val habit = backstackEntry.arguments!!.getLong("habitId")
            EditHabitScreen(
                habitId = habit,
                onBack = {
                    navController.popBackStack()
                },
                onDone = {
                    navController.popBackStack()
                }
            )
        }
        /* HABIT SCREENS END */
    }
}
