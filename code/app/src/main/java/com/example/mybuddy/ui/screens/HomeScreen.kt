package com.example.mybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.components.BlobMood
import com.example.mybuddy.ui.components.BuddyBlob
import com.example.mybuddy.ui.theme.*

@Composable
fun HomeScreen() {
    // TODO: Wird später vom HomeViewModel geholt:
    // - buddyMood based on WellbeingScore
    // - buddyName + buddyColor aus BuddyProfile DB
    // - dailyQuote von API
    // - stats von den jeweiligen Repositories

    val buddyMood = BlobMood.HAPPY  // Placeholder for testing
    val buddyName = "Buddy"         // Default

    // Greeting above the Buddy
    val greeting = if (buddyName == "Buddy") {
        "Hi, I'm Buddy!"
    } else {
        "Hi, I'm your buddy, $buddyName!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Greeting above the Buddy
        Text(
            text = greeting,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buddy Blob
        BuddyBlob(
            mood = buddyMood,
            colorTheme = BlobThemes.purple,
            modifier = Modifier.size(280.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: QuoteCard (API)

        // TODO: StatWidgets

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.headlineMedium)
    }
}