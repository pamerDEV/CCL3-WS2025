package com.example.mybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.BlobMood
import com.example.mybuddy.ui.components.BuddyBlob
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.BuddyViewModel
import com.example.mybuddy.ui.viewmodel.BuddyViewModelFactory
import com.example.mybuddy.utils.ColorUtil
import com.example.mybuddy.utils.hexToColor

@Composable
fun ProfileScreen(
    onCustomizeClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication
    val viewModel: BuddyViewModel = viewModel(
        factory = BuddyViewModelFactory(application.buddyRepository)
    )

    val buddyName by viewModel.buddyName.collectAsState()
    val buddyColorHex by viewModel.buddyColorHex.collectAsState()
    val buddyColor = hexToColor(buddyColorHex)
    val buddyColorTheme = ColorUtil.generateBlobTheme(buddyColor)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Profile",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Greeting
        Text(
            text = "Hi, I am your personal \"$buddyName\"",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Buddy Blob
        BuddyBlob(
            mood = BlobMood.HAPPY,
            colorTheme = buddyColorTheme,
            modifier = Modifier.size(240.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Customize Button
        GradientButton(
            text = "Customize",
            onClick = onCustomizeClick,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Overall Statistics Title
        Text(
            text = "Overall Statistics",
            style = TitleLargeRegular,
            color = TextPrimary
        )
    }
}