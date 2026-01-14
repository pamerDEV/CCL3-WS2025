package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.db.dao.WaterLogDao
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
    private val waterDao: WaterLogDao
) : ViewModel() {

    private val today = LocalDate.now().toString()

    val uiState: StateFlow<WaterUiState> =
        waterDao.getLast7Days()
            .map { logs ->

                val weekDates = currentWeekDates()

                val logMap = logs.associateBy { LocalDate.parse(it.date) }

                val today = LocalDate.now()
                val todayLog = logMap[today]

                val current = todayLog?.amount ?: 0
                val goal = todayLog?.goal ?: 2000

                val progress =
                    if (goal == 0) 0f else current / goal.toFloat()

                val weeklyPercentages = weekDates.map { date ->
                    val log = logMap[date]
                    if (log == null || log.goal == 0) {
                        0
                    } else {
                        ((log.amount / log.goal.toFloat()) * 100).toInt()
                            .coerceIn(0, 100)
                    }
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


    // ➕ +200ml
    fun addWater(amount: Int = 200) {
        viewModelScope.launch {

            val existing = waterDao.getForDate(today)

            val newAmount =
                (existing?.amount ?: 0) + amount

            waterDao.insert(
                WaterLogEntity(
                    id = existing?.id ?: 0,
                    date = today,
                    amount = newAmount,
                    goal = existing?.goal ?: 2000
                )
            )
        }
    }
}

class WaterViewModelFactory(
    private val waterDao: WaterLogDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WaterViewModel(waterDao) as T
    }
}

