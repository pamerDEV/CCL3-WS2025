package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.MoodRepository
import com.example.mybuddy.db.entity.MoodEntity
import com.example.mybuddy.utils.DateUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class MoodViewModel(private val repository: MoodRepository) : ViewModel() {

    val allMoods: StateFlow<List<MoodEntity>> = repository.allMoods
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addMood(moodType: String, note: String? = null) {
        addMoodWithTimestamp(moodType, note, System.currentTimeMillis())
    }

    fun addMoodWithTimestamp(moodType: String, note: String? = null, timestamp: Long) {
        viewModelScope.launch {
            // Delete existing mood for this day first
            val startOfDay = DateUtil.getStartOfDay(timestamp)
            val endOfDay = DateUtil.getEndOfDay(timestamp)
            val existingMood = repository.getMoodByDate(startOfDay, endOfDay)

            if (existingMood != null) {
                repository.deleteMood(existingMood)
            }

            // Insert new mood
            val mood = MoodEntity(
                moodType = moodType,
                note = note,
                timestamp = timestamp
            )
            repository.insertMood(mood)
        }
    }

    fun updateMood(mood: MoodEntity) {
        viewModelScope.launch {
            repository.updateMood(mood)
        }
    }

    fun deleteMood(mood: MoodEntity) {
        viewModelScope.launch {
            repository.deleteMood(mood)
        }
    }

    suspend fun getMoodById(id: Long): MoodEntity? {
        return repository.getMoodById(id)
    }

    suspend fun getTodaysMood(): MoodEntity? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis

        return repository.getMoodByDate(startOfDay, endOfDay)
    }
}

class MoodViewModelFactory(private val repository: MoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}