package com.example.mybuddy.ui.components.habit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.mybuddy.ui.theme.Surface
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary
import com.example.mybuddy.ui.theme.Violet
import com.example.mybuddy.ui.viewmodel.habit.HabitUiState

@Composable
fun HabitCard(
    state: HabitUiState, onCheckIn: () -> Unit, onEdit: () -> Unit
) {
    val streakDates = remember { currentMonthDates() }

    val completedDates = remember(state.logs) {
        state.logs.filter { it.completed }.map { it.date }.toSet()
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HabitStreakGrid(
                dates = streakDates,
                completedDates = completedDates,
                color = Color(state.habit.color.toColorInt())
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Habit",
                            tint = TextSecondary
                        )
                    }
                }

                state.habit.description?.let {
                    Text(
                        text = it, style = MaterialTheme.typography.bodySmall, color = TextSecondary
                    )
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = onCheckIn, colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.completedToday) Gray else Violet
                    ), modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (state.completedToday) "↺ Undo" else "+ Check in"
                    )
                }
            }
        }
    }
}