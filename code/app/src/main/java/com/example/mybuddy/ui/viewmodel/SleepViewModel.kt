package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.SleepRepository
import com.example.mybuddy.db.entity.SleepEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class SleepUiState(
    val todaySleep: SleepEntity? = null,
    val goalMinutes: Int = 480,
    val hasLoggedToday: Boolean = false
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
                hasLoggedToday = todaySleep != null
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SleepUiState()
        )

    fun addSleep(bedtime: String, wakeTime: String, quality: String) {
        viewModelScope.launch {
            val existing = repository.getSleepByDate(today)
            val durationMinutes = calculateDuration(bedtime, wakeTime)

            val sleep = SleepEntity(
                id = existing?.id ?: 0,
                date = today,
                bedtime = bedtime,
                wakeTime = wakeTime,
                quality = quality,
                durationMinutes = durationMinutes,
                goalMinutes = existing?.goalMinutes ?: 480
            )
            repository.insertSleep(sleep)
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