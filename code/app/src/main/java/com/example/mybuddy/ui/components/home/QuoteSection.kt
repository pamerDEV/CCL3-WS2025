package com.example.mybuddy.ui.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.data.quote.QuoteApiProvider
import com.example.mybuddy.ui.viewmodel.QuoteViewModel
import com.example.mybuddy.ui.viewmodel.QuoteViewModelFactory

@Composable
fun QuoteSection() {
    val viewModel: QuoteViewModel = viewModel(
        factory = QuoteViewModelFactory(
            QuoteApiProvider.api
        )
    )

    val quote by viewModel.quote.collectAsState()

    if (quote == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(strokeWidth = 2.dp)
        }
    } else {
        QuoteBubble(text = quote!!)
    }
}