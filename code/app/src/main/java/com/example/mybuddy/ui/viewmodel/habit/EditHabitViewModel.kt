package com.example.mybuddy.ui.viewmodel.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.HabitRepository
import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.entity.HabitEntity
import kotlinx.coroutines.launch

class EditHabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    fun updateHabit(
        habit: HabitEntity, name: String, description: String, colorHex: String, onDone: () -> Unit
    ) {
        viewModelScope.launch {
            repository.updateHabit(
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
            repository.deleteHabit(habit)
            onDone()
        }
    }
}

class EditHabitViewModelFactory(
    private val repository: HabitRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditHabitViewModel(repository) as T
    }
}