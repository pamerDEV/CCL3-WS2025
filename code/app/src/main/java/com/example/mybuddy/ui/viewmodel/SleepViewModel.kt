package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.SleepRepository
import com.example.mybuddy.db.entity.SleepEntity
import com.example.mybuddy.ui.components.health.sleep.SleepDayData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class SleepUiState(
    val todaySleep: SleepEntity? = null,
    val goalMinutes: Int = 480,
    val hasLoggedToday: Boolean = false,
    val weeklyData: List<SleepDayData> = emptyList()
)

class SleepViewModel(
    private val repository: SleepRepository
) : ViewModel() {

    private val today = LocalDate.now().toString()

    val uiState: StateFlow<SleepUiState> = repository.getAllSleepLogs()
        .map { logs ->
            val todaySleep = logs.find { it.date == today }
            SleepUiState(
                todaySleep = todaySleep,
                goalMinutes = todaySleep?.goalMinutes ?: 480,
                hasLoggedToday = todaySleep != null,
                weeklyData = calculateWeeklyData(logs)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SleepUiState()
        )

    private fun calculateWeeklyData(logs: List<SleepEntity>): List<SleepDayData> {
        val today = LocalDate.now()
        val last7Days = (6 downTo 0).map { today.minusDays(it.toLong()) }

        return last7Days.map { date ->
            val log = logs.find { it.date == date.toString() }

            SleepDayData(
                dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US).take(2),
                durationHours = (log?.durationMinutes ?: 0) / 60f,
                goalHours = (log?.goalMinutes ?: 480) / 60f,
                quality = log?.quality
            )
        }
    }

    fun addSleep(bedtime: String, wakeTime: String, quality: String) {
        viewModelScope.launch {
            val durationMinutes = calculateDuration(bedtime, wakeTime)

            val sleep = SleepEntity(
                id = 0,
                date = today,
                bedtime = bedtime,
                wakeTime = wakeTime,
                quality = quality,
                durationMinutes = durationMinutes,
                goalMinutes = 480
            )
            repository.insertSleep(sleep)
        }
    }

    fun updateSleep(bedtime: String, wakeTime: String, quality: String) {
        viewModelScope.launch {
            val existing = repository.getSleepByDate(today)
            existing?.let {
                val durationMinutes = calculateDuration(bedtime, wakeTime)

                val updatedSleep = it.copy(
                    bedtime = bedtime,
                    wakeTime = wakeTime,
                    quality = quality,
                    durationMinutes = durationMinutes
                )
                repository.updateSleep(updatedSleep)
            }
        }
    }

    fun deleteSleep() {
        viewModelScope.launch {
            val existing = repository.getSleepByDate(today)
            existing?.let { repository.deleteSleep(it) }
        }
    }

    private fun calculateDuration(bedtime: String, wakeTime: String): Int {
        val (bedHour, bedMin) = bedtime.split(":").map { it.toInt() }
        val (wakeHour, wakeMin) = wakeTime.split(":").map { it.toInt() }

        val bedMinutes = bedHour * 60 + bedMin
        val wakeMinutes = wakeHour * 60 + wakeMin

        return if (wakeMinutes >= bedMinutes) {
            wakeMinutes - bedMinutes
        } else {
            (24 * 60 - bedMinutes) + wakeMinutes
        }
    }
}

class SleepViewModelFactory(
    private val repository: SleepRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SleepViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}