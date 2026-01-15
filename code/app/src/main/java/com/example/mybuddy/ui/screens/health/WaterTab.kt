package com.example.mybuddy.ui.screens.health

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.components.health.water.AddWaterButton
import com.example.mybuddy.ui.components.health.water.AddWaterDialog
import com.example.mybuddy.ui.components.health.water.WaterProgressRing
import com.example.mybuddy.ui.components.health.water.WeeklyWaterStats
import com.example.mybuddy.ui.theme.GradientBlueEnd
import com.example.mybuddy.ui.theme.HabitBlue
import com.example.mybuddy.ui.theme.TextSecondary
import com.example.mybuddy.ui.viewmodel.WaterViewModel

@Composable
fun WaterTab(
    viewModel: WaterViewModel
) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daily Water Tracker",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            Row {
                Text("Daily goal: ", color = TextSecondary)
                Text(
                    text = "${state.currentMl}/${state.goalMl}ml",
                    color = GradientBlueEnd
                )
            }

            Spacer(Modifier.height(32.dp))

            WaterProgressRing(progress = state.progress)

            Spacer(Modifier.height(32.dp))

            WeeklyWaterStats(state.weeklyPercentages)
        }

        // Floating Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            AddWaterButton(
                onClick = { showDialog = true }
            )
        }
    }

    if (showDialog) {
        AddWaterDialog(
            onDismiss = { showDialog = false },
            onAdd = { amount ->
                viewModel.addWater(amount)
                showDialog = false
            },
            dailyGoal = state.goalMl  // Vom ViewModel
        )
    }
}