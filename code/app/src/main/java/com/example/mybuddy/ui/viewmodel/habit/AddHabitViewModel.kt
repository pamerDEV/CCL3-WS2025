package com.example.mybuddy.ui.viewmodel.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.db.dao.HabitDao
import com.example.mybuddy.db.entity.HabitEntity
import kotlinx.coroutines.launch

class AddHabitViewModel(
    private val habitDao: HabitDao
) : ViewModel() {

    fun createHabit(
        title: String, description: String, colorHex: String, onDone: () -> Unit
    ) {
        viewModelScope.launch {
            habitDao.insertHabit(
                HabitEntity(
                    name = title, description = description, color = colorHex
                )
            )
            onDone()
        }
    }
}

class AddHabitViewModelFactory(
    private val habitDao: HabitDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddHabitViewModel(habitDao) as T
    }
}