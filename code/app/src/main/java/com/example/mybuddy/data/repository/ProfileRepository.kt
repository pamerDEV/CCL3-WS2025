package com.example.mybuddy.data.repository

import android.util.Log
import com.example.mybuddy.db.entity.HabitLogEntity
import com.example.mybuddy.db.entity.MoodEntity
import com.example.mybuddy.db.entity.SleepEntity
import com.example.mybuddy.db.entity.WaterLogEntity
import com.example.mybuddy.ui.viewmodel.ProfileStatsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import java.time.LocalDate

private fun Long.toLocalDate(): LocalDate =
    java.time.Instant.ofEpochMilli(this)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()

class ProfileRepository(
    private val habitRepository: HabitRepository,
    private val moodRepository: MoodRepository,
    private val sleepRepository: SleepRepository,
    private val waterRepository: WaterRepository
) {

    fun getProfileStats(): Flow<ProfileStatsState> =
        combine(
            habitRepository.allLogs,
            habitRepository.getAllHabits(),
            moodRepository.allMoods,
            sleepRepository.allLogs,
            waterRepository.allLogs
        ) { habitLogs, habits, moods, sleepLogs, waterLogs ->
            ProfileStatsState(
                activeDays = calculateActiveDays(
                    habitLogs = habitLogs,
                    totalHabits = habits.size,
                    sleepLogs = sleepLogs,
                    moods = moods,
                    waterLogs = waterLogs
                ),

                highestStreak = calculateHighestStreak(habitLogs),

                commonMood = moods
                    .groupingBy { it.moodType }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key ?: "—",

                averageSleepHours =
                    if (sleepLogs.isEmpty()) 0f
                    else sleepLogs.map { it.durationMinutes }.average().toFloat() / 60
            )
        }
}

private fun calculateHighestStreak(
    logs: List<HabitLogEntity>
): Int {
    val dates = logs
        .filter { it.completed }
        .map { LocalDate.parse(it.date) }
        .distinct()
        .sorted()

    var current = 0
    var max = 0

    for (i in dates.indices) {
        if (i == 0 || dates[i] == dates[i - 1].plusDays(1)) {
            current++
        } else {
            current = 1
        }
        max = maxOf(max, current)
    }

    return max
}

private fun calculateActiveDays(
    habitLogs: List<HabitLogEntity>,
    totalHabits: Int,
    sleepLogs: List<SleepEntity>,
    moods: List<MoodEntity>,
    waterLogs: List<WaterLogEntity>
): Int {

    val allDays: Set<LocalDate> = (
            habitLogs.map { LocalDate.parse(it.date) } +
                    sleepLogs.map { LocalDate.parse(it.date) } +
                    waterLogs.map { LocalDate.parse(it.date) } +
                    moods.map { it.timestamp.toLocalDate() }
            ).toSet()

    return allDays.count { day ->

        val habitsDone =
            habitLogs
                .filter { LocalDate.parse(it.date) == day && it.completed }
                .map { it.habitId }
                .distinct()
                .count() == totalHabits

        val hasSleep =
            sleepLogs.any { LocalDate.parse(it.date) == day }

        val hasMood =
            moods.any { it.timestamp.toLocalDate() == day }

        val waterDone =
            waterLogs.any {
                LocalDate.parse(it.date) == day &&
                        it.amount >= it.goal
            }

        habitsDone && hasSleep && hasMood && waterDone
    }
}