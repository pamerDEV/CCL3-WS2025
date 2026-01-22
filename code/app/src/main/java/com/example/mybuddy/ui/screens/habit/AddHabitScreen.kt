package com.example.mybuddy.ui.screens.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.habit.AddHabitViewModel
import com.example.mybuddy.ui.viewmodel.habit.AddHabitViewModelFactory
import com.example.mybuddy.utils.toHexString

@Composable
fun AddHabitScreen(
    onBack: () -> Unit, onHabitCreated: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as MyBuddyApplication

    val viewModel: AddHabitViewModel = viewModel(
        factory = AddHabitViewModelFactory(app.habitRepository)
    )

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val colors = listOf(
        HabitRed, HabitOrange, HabitYellow, HabitGreen, HabitBlue, HabitIndigo, HabitPink, HabitTeal
    )

    var selectedColor by remember { mutableStateOf(colors.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Create new Habit",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        Text("Habit Name", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("What do you want to do daily?") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text("Description (optional)", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Small description") },
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

        GradientButton(
            text = "Create Habit",
            onClick = {
                viewModel.createHabit(
                    title = name,
                    description = description,
                    colorHex = selectedColor.toHexString(),
                    onDone = onHabitCreated
                )
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}