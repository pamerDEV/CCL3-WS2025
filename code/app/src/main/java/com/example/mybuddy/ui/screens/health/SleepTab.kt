package com.example.mybuddy.ui.screens.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.components.SleepQuality
import com.example.mybuddy.ui.components.health.sleep.SleepClockRing
import com.example.mybuddy.ui.components.health.sleep.SleepInfoCard
import com.example.mybuddy.ui.components.health.sleep.SleepQualityCard
import com.example.mybuddy.ui.components.health.sleep.WeeklySleepStats
import com.example.mybuddy.ui.theme.HabitGreen
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary
import com.example.mybuddy.ui.theme.Violet
import com.example.mybuddy.ui.viewmodel.SleepViewModel

@Composable
fun SleepTab(
    viewModel: SleepViewModel,
    onAddSleepClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val todaySleep = state.todaySleep

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Last Night's Sleep",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            // Daily Goal - immer Violet
            Row {
                Text("Daily goal: ", color = TextSecondary)
                if (todaySleep != null) {
                    val hours = todaySleep.durationMinutes / 60
                    val mins = todaySleep.durationMinutes % 60
                    val goalHours = state.goalMinutes / 60
                    Text(
                        text = "${hours}h ${mins}m / ${goalHours}h",
                        color = Violet
                    )
                } else {
                    Text(
                        text = "-- / ${state.goalMinutes / 60}h",
                        color = Violet
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Clock Ring
            if (todaySleep != null) {
                SleepClockRing(
                    durationMinutes = todaySleep.durationMinutes,
                    bedtime = todaySleep.bedtime,
                    wakeTime = todaySleep.wakeTime,
                    quality = todaySleep.quality
                )

                Spacer(Modifier.height(24.dp))

                // Bedtime / Wakeup Cards
                Row(
                    modifier = Modifier.width(420.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SleepInfoCard(
                        label = "Bedtime",
                        value = todaySleep.bedtime,
                        valueColor = Violet,
                        modifier = Modifier.weight(1f)
                    )
                    SleepQualityCard(
                        quality = SleepQuality.fromString(todaySleep.quality),
                        modifier = Modifier.weight(1f)
                    )
                    SleepInfoCard(
                        label = "Wakeup",
                        value = todaySleep.wakeTime,
                        valueColor = HabitGreen,
                        modifier = Modifier.weight(1f)
                    )

                }

                Spacer(Modifier.height(12.dp))

            } else {
                // No sleep logged - zeige leeren Clock Ring
                SleepClockRing(
                    durationMinutes = null,
                    bedtime = null,
                    wakeTime = null,
                    quality = null
                )
            }

            Spacer(Modifier.height(24.dp))

            // Weekly Sleep Stats
            WeeklySleepStats(
                weeklyData = state.weeklyData
            )
        }

        // Floating Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            GradientButton(
                text = if (todaySleep != null) "Edit Today's Sleep" else "Add Today's Sleep",
                onClick = onAddSleepClick
            )
        }
    }
}