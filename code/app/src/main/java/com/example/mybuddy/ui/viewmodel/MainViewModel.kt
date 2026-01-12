package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mybuddy.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _currentRoute = MutableStateFlow(Screen.Home.route)
    val currentRoute: StateFlow<String> = _currentRoute

    fun onNavigate(route: String) {
        _currentRoute.value = route
    }
}
