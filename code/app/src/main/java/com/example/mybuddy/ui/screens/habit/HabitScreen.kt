package com.example.mybuddy.ui.screens.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.habit.HabitUiState
import com.example.mybuddy.ui.viewmodel.habit.HabitViewModel
import com.example.mybuddy.ui.viewmodel.habit.HabitsViewModelFactory
import java.time.LocalDate
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.mybuddy.ui.components.habit.HabitCard
import com.example.mybuddy.ui.components.habit.StatCard

@Composable
fun HabitScreen(
    onCreateHabit: () -> Unit = {}, navController: NavController
) {
    val context = LocalContext.current
    val app = context.applicationContext as MyBuddyApplication

    val viewModel: HabitViewModel = viewModel(
        factory = HabitsViewModelFactory(
            app.database.habitDao(), app.database.habitLogDao()
        )
    )

    val habits by viewModel.habits.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "My Daily Habits",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Build routines that will lead you to success.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                "Total Habits", habits.size.toString(), HabitGreen
            )
            StatCard(
                "Completed Today", habits.count { it.completedToday }.toString(), HabitBlue
            )
        }

        Spacer(Modifier.height(16.dp))

        GradientButton(
            text = "Create new Habit", onClick = onCreateHabit
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(habits) { habitState ->
                HabitCard(state = habitState, onCheckIn = {
                    viewModel.toggleCheckIn(
                        habitState.habit.id, !habitState.completedToday
                    )
                }, onEdit = {
                    navController.navigate("edit_habit/${habitState.habit.id}")
                })
            }
        }
    }
}
