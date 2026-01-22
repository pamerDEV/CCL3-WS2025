package com.example.mybuddy.ui.screens.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    onCreateHabit: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val app = context.applicationContext as MyBuddyApplication

    val viewModel: HabitViewModel = viewModel(
        factory = HabitsViewModelFactory(app.habitRepository)
    )

    val habits by viewModel.habits.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        item {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "My Daily Habits",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Build routines that will lead you to success.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    "Total Habits",
                    habits.size.toString(),
                    HabitBlue
                )
                StatCard(
                    "Completed Today",
                    habits.count { it.completedToday }.toString(),
                    HabitGreen
                )
            }

            Spacer(Modifier.height(16.dp))

            GradientButton(
                text = "Create new Habit",
                onClick = onCreateHabit
            )

            Spacer(Modifier.height(28.dp))
        }

        // HABIT LIST
        items(
            items = habits,
            key = { it.habit.id }
        ) { habitState ->
            HabitCard(
                state = habitState,
                onCheckIn = {
                    viewModel.toggleCheckIn(
                        habitState.habit.id,
                        !habitState.completedToday
                    )
                },
                onEdit = {
                    navController.navigate("edit_habit/${habitState.habit.id}")
                }
            )
        }

        item {
            Spacer(Modifier.height(80.dp))
        }
    }
}