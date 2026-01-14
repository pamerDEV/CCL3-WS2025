package com.example.mybuddy.ui.components.mood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mybuddy.db.entity.MoodEntity
import com.example.mybuddy.ui.theme.HabitBlue
import com.example.mybuddy.ui.theme.HabitGreen
import com.example.mybuddy.ui.theme.HabitOrange
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.utils.DateUtil
import java.util.Calendar

@Composable
fun MoodStatsRow(
    moods: List<MoodEntity>,
    year: Int,
    month: Int
) {
    // Filter moods for selected month
    val monthMoods = moods.filter { mood ->
        val calendar = Calendar.getInstance().apply {
            timeInMillis = mood.timestamp
        }
        val moodYear = calendar.get(Calendar.YEAR)
        val moodMonth = calendar.get(Calendar.MONTH)
        moodYear == year && moodMonth == month
    }

    // Entries for selected month
    val entries = monthMoods.size

    // Positive percentage for selected month
    val positiveMoods = listOf("AWESOME", "LOVED", "OKAY")
    val positiveCount = monthMoods.count { positiveMoods.contains(it.moodType) }
    val positivePercent = if (entries > 0) (positiveCount * 100) / entries else 0

    // Streak for selected month
    val streak = calculateMonthStreak(monthMoods, year, month)

    Column {
        Text(
            text = "This month",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MoodStatCard(
                label = "entries",
                value = entries.toString(),
                valueColor = HabitGreen,
                modifier = Modifier.weight(1f)
            )
            MoodStatCard(
                label = "positive",
                value = "$positivePercent%",
                valueColor = HabitBlue,
                modifier = Modifier.weight(1f)
            )
            MoodStatCard(
                label = "streak",
                value = streak.toString(),
                valueColor = HabitOrange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun calculateMonthStreak(monthMoods: List<MoodEntity>, year: Int, month: Int): Int {
    if (monthMoods.isEmpty()) return 0

    // Get all days in the month that have moods
    val daysWithMood = monthMoods.map { mood ->
        val calendar = Calendar.getInstance().apply {
            timeInMillis = mood.timestamp
        }
        calendar.get(Calendar.DAY_OF_MONTH)
    }.toSet()

    // Find longest consecutive streak in the month
    var maxStreak = 0
    var currentStreak = 0
    val daysInMonth = DateUtil.getDaysInMonth(year, month)

    for (day in 1..daysInMonth) {
        if (daysWithMood.contains(day)) {
            currentStreak++
            maxStreak = maxOf(maxStreak, currentStreak)
        } else {
            currentStreak = 0
        }
    }

    return maxStreak
}