package com.example.mybuddy.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.mybuddy.ui.components.profile.ProfileStatCard
import com.example.mybuddy.ui.components.profile.SettingsRow
import com.example.mybuddy.ui.components.profile.SleepGoalDialog
import com.example.mybuddy.ui.components.profile.WaterGoalDialog
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.BuddyViewModel
import com.example.mybuddy.ui.viewmodel.BuddyViewModelFactory
import com.example.mybuddy.ui.viewmodel.ProfileViewModel
import com.example.mybuddy.ui.viewmodel.ProfileViewModelFactory
import com.example.mybuddy.ui.viewmodel.UserSettingsViewModel
import com.example.mybuddy.ui.viewmodel.UserSettingsViewModelFactory
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

    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(application.profileRepository)
    )

    val settingsViewModel: UserSettingsViewModel = viewModel(
        factory = UserSettingsViewModelFactory(
            application.userSettingsRepository
        )
    )

    val stats by profileViewModel.stats.collectAsState()
    val settings by settingsViewModel.uiState.collectAsState()

    val buddyName by viewModel.buddyName.collectAsState()
    val buddyColorHex by viewModel.buddyColorHex.collectAsState()
    val buddyColor = hexToColor(buddyColorHex)
    val buddyColorTheme = ColorUtil.generateBlobTheme(buddyColor)

    var showWaterDialog by remember { mutableStateOf(false) }
    var showSleepDialog by remember { mutableStateOf(false) }

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

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    var day = "days"

                    if(stats.activeDays == 1) {
                        day = "day"
                    }

                    ProfileStatCard(
                        title = "Perfect days",
                        emoji = "🌟",
                        value = stats.activeDays.toString() + " " + day
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    var day = "days"

                    if(stats.highestStreak == 1) {
                        day = "day"
                    }

                    ProfileStatCard(
                        title = "Habit streak",
                        emoji = "🔥",
                        value = stats.highestStreak.toString() + " " + day
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ProfileStatCard(
                        title = "Common mood",
                        emoji = "🥰",
                        value = stats.commonMood
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    ProfileStatCard(
                        title = "Average sleep",
                        emoji = "😴",
                        value = String.format(java.util.Locale.US, "%.1fh", stats.averageSleepHours)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Settings",
            style = TitleLargeRegular,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Water goal",
            value = "${settings.waterGoalMl}ml",
            onClick = { showWaterDialog = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsRow(
            title = "Sleep goal",
            value = "${settings.sleepGoalMinutes / 60}h",
            onClick = { showSleepDialog = true }
        )

        if (showWaterDialog) {
            WaterGoalDialog(
                current = settings.waterGoalMl,
                onDismiss = { showWaterDialog = false },
                onSave = { newGoal ->
                    settingsViewModel.setWaterGoal(newGoal)
                    showWaterDialog = false
                }
            )
        }

        if (showSleepDialog) {
            SleepGoalDialog(
                currentMinutes = settings.sleepGoalMinutes,
                onDismiss = { showSleepDialog = false },
                onSave = { minutes ->
                    settingsViewModel.setSleepGoal(minutes)
                    showSleepDialog = false
                }
            )
        }
    }
}
