package com.example.mybuddy.ui.components.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import java.time.LocalDate

fun currentMonthDates(): List<String> {
    val today = LocalDate.now()
    return (48 downTo 0).map { offset ->
        today.minusDays(offset.toLong()).toString()
    }
}

@Composable
fun HabitStreakGrid(
    dates: List<String>, completedDates: Set<String>, color: Color
) {
    val columns = 7

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        dates.chunked(columns).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { date ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (completedDates.contains(date)) color else Gray,
                                shape = RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }
    }
}