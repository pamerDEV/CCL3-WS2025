package com.example.mybuddy.ui.viewmodel.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.HabitRepository
import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.dao.HabitLogDao
import com.example.mybuddy.db.entity.HabitEntity
import com.example.mybuddy.db.entity.HabitLogEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitUiState(
    val habit: HabitEntity, val logs: List<HabitLogEntity>, val completedToday: Boolean
)

class HabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    val habits: StateFlow<List<HabitUiState>> = repository.getAllHabits().flatMapLatest { habits ->

            if (habits.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(
                    habits.map { habit ->
                        repository.getLogsForHabit(habit.id).map { logs ->
                                val today = LocalDate.now().toString()
                                val completedToday = logs.any {
                                    it.date == today && it.completed
                                }

                                HabitUiState(
                                    habit = habit, logs = logs, completedToday = completedToday
                                )
                            }
                    }) { uiStates ->
                    uiStates.toList()
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun toggleCheckIn(habitId: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.upsertLog(
                HabitLogEntity(
                    habitId = habitId, date = LocalDate.now().toString(), completed = completed
                )
            )
        }
    }
}


class HabitsViewModelFactory(
    private val repository: HabitRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitViewModel(repository) as T
    }
}
