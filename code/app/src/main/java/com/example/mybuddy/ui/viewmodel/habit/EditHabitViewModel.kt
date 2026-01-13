package com.example.mybuddy.ui.viewmodel.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.entity.HabitEntity
import kotlinx.coroutines.launch

class EditHabitViewModel(
    private val habitDao: HabitDao
) : ViewModel() {

    fun updateHabit(
        habit: HabitEntity, name: String, description: String, colorHex: String, onDone: () -> Unit
    ) {
        viewModelScope.launch {
            habitDao.updateHabit(
                habit.copy(
                    name = name, description = description, color = colorHex
                )
            )
            onDone()
        }
    }

    fun deleteHabit(
        habit: HabitEntity, onDone: () -> Unit
    ) {
        viewModelScope.launch {
            habitDao.deleteHabit(habit)
            onDone()
        }
    }
}

class EditHabitViewModelFactory(
    private val habitDao: HabitDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditHabitViewModel(habitDao) as T
    }
}