package com.example.mybuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mybuddy.data.quote.QuoteApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel(
    private val api: QuoteApi
) : ViewModel() {

    private val _quote = MutableStateFlow<String?>(null)
    val quote: StateFlow<String?> = _quote

    init {
        loadQuote()
    }

    fun loadQuote() {
        viewModelScope.launch {
            try {
                val result = api.getRandomQuote()
                _quote.value = result.first().q
            } catch (e: Exception) {
                _quote.value = "You're doing great. Keep going!"
            }
        }
    }
}

class QuoteViewModelFactory(
    private val api: QuoteApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuoteViewModel(api) as T
    }
}
