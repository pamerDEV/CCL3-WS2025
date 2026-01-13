package com.example.mybuddy.ui.screens.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.db.entity.HabitEntity
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.habit.EditHabitViewModel
import com.example.mybuddy.ui.viewmodel.habit.EditHabitViewModelFactory
import com.example.mybuddy.utils.hexToColor
import com.example.mybuddy.utils.toHexString

@Composable
fun EditHabitScreen(
    habitId: Long, onBack: () -> Unit, onDone: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as MyBuddyApplication

    val habit by produceState<HabitEntity?>(
        initialValue = null, key1 = habitId
    ) {
        value = app.database.habitDao().getHabitById(habitId)
    }

    val viewModel: EditHabitViewModel = viewModel(
        factory = EditHabitViewModelFactory(app.database.habitDao())
    )

    when (habit) {
        null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Violet)
            }
        }

        else -> {
            EditHabitContent(
                habit = habit!!, viewModel = viewModel, onBack = onBack, onDone = onDone
            )
        }
    }
}

@Composable
private fun EditHabitContent(
    habit: HabitEntity, viewModel: EditHabitViewModel, onBack: () -> Unit, onDone: () -> Unit
) {
    var name by remember(habit.id) { mutableStateOf(habit.name) }
    var description by remember(habit.id) {
        mutableStateOf(habit.description ?: "")
    }
    var selectedColor by remember(habit.id) {
        mutableStateOf(hexToColor(habit.color))
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val colors = listOf(
        HabitRed, HabitOrange, HabitYellow, HabitGreen, HabitBlue, HabitIndigo, HabitPink, HabitTeal
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {

        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Edit Daily Habit",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(Modifier.height(24.dp))

        Text("Habit Name", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text("Description", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text("Select Color", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(color, CircleShape)
                        .border(
                            width = if (color == selectedColor) 3.dp else 0.dp,
                            color = TextPrimary,
                            shape = CircleShape
                        )
                        .clickable { selectedColor = color }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            GradientButton(
                text = "Save",
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.updateHabit(
                        habit = habit,
                        name = name,
                        description = description,
                        colorHex = selectedColor.toHexString(),
                        onDone = onDone
                    )
                }
            )

            GradientButton(
                text = "Delete",
                modifier = Modifier.weight(1f),
                onClick = {
                    showDeleteDialog = true
                },
                gradientColors = listOf(DeleteRedLight, DeleteRed)
            )
        }

        Spacer(Modifier.height(24.dp))
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Habit") },
            text = { Text("Are you sure you want to delete this habit? All streak data will be lost.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteHabit(habit, onDone)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = DeleteRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}