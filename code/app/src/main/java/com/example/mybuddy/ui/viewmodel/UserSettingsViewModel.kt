package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.SleepRepository
import com.example.mybuddy.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val waterGoalMl: Int = 2000,
    val sleepGoalMinutes: Int = 8 * 60
)

class UserSettingsViewModel(
    private val repository: UserSettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        repository.settings
            .map {
                SettingsUiState(
                    waterGoalMl = it.waterGoalMl,
                    sleepGoalMinutes = it.sleepGoalMinutes
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                SettingsUiState()
            )

    fun setWaterGoal(goal: Int) {
        viewModelScope.launch {
            repository.updateWaterGoal(goal)
        }
    }

    fun setSleepGoal(minutes: Int) {
        viewModelScope.launch {
            repository.updateSleepGoal(minutes)
        }
    }
}

class UserSettingsViewModelFactory(
    private val repository: UserSettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserSettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}