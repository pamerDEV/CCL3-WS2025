package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.UserSettingsRepository
import com.example.mybuddy.data.repository.WaterRepository
import com.example.mybuddy.db.entity.WaterLogEntity
import com.example.mybuddy.utils.DateUtil.currentWeekDates
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

data class WaterUiState(
    val currentMl: Int = 0,
    val goalMl: Int = 2000,
    val progress: Float = 0f,
    val weeklyPercentages: List<Int> = emptyList()
)

class WaterViewModel(
    private val repository: WaterRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val today = LocalDate.now().toString()

    val uiState: StateFlow<WaterUiState> =
        combine(
            repository.getLast7Days(),
            userSettingsRepository.settings
        ) { logs, settings ->

            val weekDates = currentWeekDates()
            val logMap = logs.associateBy { LocalDate.parse(it.date) }
            val todayDate = LocalDate.now()
            val todayLog = logMap[todayDate]

            val goal = settings.waterGoalMl
            val current = todayLog?.amount ?: 0
            val progress =
                if (goal == 0) 0f else current / goal.toFloat()

            val weeklyPercentages = weekDates.map { date ->
                val log = logMap[date]
                if (log == null || goal == 0) 0
                else ((log.amount / goal.toFloat()) * 100).toInt().coerceIn(0, 100)
            }

            WaterUiState(
                currentMl = current,
                goalMl = goal,
                progress = progress.coerceIn(0f, 1f),
                weeklyPercentages = weeklyPercentages
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = WaterUiState()
            )

    fun addWater(amount: Int = 200) {
        viewModelScope.launch {
            val existing = repository.getForDate(today)
            repository.insert(
                WaterLogEntity(
                    id = existing?.id ?: 0,
                    date = today,
                    amount = (existing?.amount ?: 0) + amount,
                    goal = existing?.goal ?: 0
                )
            )
        }
    }
}

class WaterViewModelFactory(
    private val repository: WaterRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WaterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WaterViewModel(repository, userSettingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}