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
            MoodScreen(
                onAddMoodClick = {
                    navController.navigate(Screen.AddMood.createRoute())
                },
                onDayClick = { timestamp, _ ->
                    navController.navigate(Screen.MoodDayView.createRoute(timestamp))
                }
            )
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

        /*Mood Screen Start*/
        composable(Screen.AddMood.route) {
            AddMoodScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.MoodDayView.route,
            arguments = listOf(
                navArgument("timestamp") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val timestamp = backStackEntry.arguments?.getLong("timestamp") ?: 0L
            MoodDayView(
                timestamp = timestamp,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { moodId ->
                    navController.navigate(Screen.EditMood.createRoute(moodId))
                },
                onAddClick = { ts ->
                    navController.navigate(Screen.AddMood.createRoute(ts))
                },
                onDeleteSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.EditMood.route,
            arguments = listOf(
                navArgument("moodId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val moodId = backStackEntry.arguments?.getLong("moodId") ?: 0L
            EditMoodScreen(
                moodId = moodId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "add_mood?timestamp={timestamp}",
            arguments = listOf(
                navArgument("timestamp") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val timestamp = backStackEntry.arguments?.getLong("timestamp")
            AddMoodScreen(
                timestamp = if (timestamp == 0L) null else timestamp,
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    navController.popBackStack()
                }
            )
        }
        /*Mood Screen End*/


    }
}
