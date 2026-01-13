package com.example.mybuddy.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.repository.BuddyRepository
import com.example.mybuddy.db.entity.BuddyProfileEntity
import com.example.mybuddy.ui.theme.Violet
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BuddyViewModel(private val repository: BuddyRepository) : ViewModel() {

    private val _buddyProfile = repository.buddyProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val buddyName: StateFlow<String> = _buddyProfile
        .map { it?.name ?: "Buddy" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Buddy")

    val buddyColorHex: StateFlow<String> = _buddyProfile
        .map { it?.colorHex ?: "#9756F2" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "#9756F2")

    fun saveBuddyProfile(name: String, colorHex: String) {
        viewModelScope.launch {
            repository.saveBuddyProfile(name, colorHex)
        }
    }
}

class BuddyViewModelFactory(private val repository: BuddyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BuddyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BuddyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}