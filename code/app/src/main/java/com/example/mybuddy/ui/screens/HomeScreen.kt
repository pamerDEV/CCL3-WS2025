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
import com.example.mybuddy.ui.components.MoodType
import com.example.mybuddy.ui.components.home.HabitsStatCard
import com.example.mybuddy.ui.components.home.MoodStatCard
import com.example.mybuddy.ui.components.api.QuoteSection
import com.example.mybuddy.ui.components.home.SleepStatCard
import com.example.mybuddy.ui.components.home.WaterStatCard
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.BuddyViewModel
import com.example.mybuddy.ui.viewmodel.BuddyViewModelFactory
import com.example.mybuddy.ui.viewmodel.MoodViewModel
import com.example.mybuddy.ui.viewmodel.MoodViewModelFactory
import com.example.mybuddy.ui.viewmodel.SleepViewModel
import com.example.mybuddy.ui.viewmodel.SleepViewModelFactory
import com.example.mybuddy.ui.viewmodel.WaterViewModel
import com.example.mybuddy.ui.viewmodel.WaterViewModelFactory
import com.example.mybuddy.ui.viewmodel.habit.HabitViewModel
import com.example.mybuddy.ui.viewmodel.habit.HabitsViewModelFactory
import com.example.mybuddy.utils.ColorUtil
import com.example.mybuddy.utils.DateUtil
import com.example.mybuddy.utils.WellbeingCalculator
import com.example.mybuddy.utils.hexToColor

@Composable
fun HomeScreen(
    onHabitsClick: () -> Unit = {},
    onMoodClick: () -> Unit = {},
    onWaterClick: () -> Unit = {},
    onSleepClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication

    val buddyViewModel: BuddyViewModel = viewModel(
        factory = BuddyViewModelFactory(application.buddyRepository)
    )

    val habitViewModel: HabitViewModel = viewModel(
        factory = HabitsViewModelFactory(application.habitRepository)
    )

    val moodViewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(application.moodRepository)
    )

    val waterViewModel: WaterViewModel = viewModel(
        factory = WaterViewModelFactory(application.waterRepository, application.userSettingsRepository)
    )

    val sleepViewModel: SleepViewModel = viewModel(
        factory = SleepViewModelFactory(application.sleepRepository, application.userSettingsRepository)
    )

    val buddyName by buddyViewModel.buddyName.collectAsState()
    val buddyColorHex by buddyViewModel.buddyColorHex.collectAsState()
    val habits by habitViewModel.habits.collectAsState()
    val moods by moodViewModel.allMoods.collectAsState()
    val waterState by waterViewModel.uiState.collectAsState()
    val sleepState by sleepViewModel.uiState.collectAsState()

    val todayMood = moods.find { mood ->
        DateUtil.isToday(mood.timestamp)
    }?.let { MoodType.fromString(it.moodType) }

    // Calculate Wellbeing Score
    val wellbeingScore = WellbeingCalculator.calculateScore(
        todayMood = todayMood,
        habitsCompleted = habits.count { it.completedToday },
        totalHabits = habits.size,
        sleepDurationMinutes = sleepState.todaySleep?.durationMinutes,
        sleepGoalMinutes = sleepState.goalMinutes,
        sleepQuality = sleepState.todaySleep?.quality,
        waterIntakeMl = waterState.currentMl,
        waterGoalMl = waterState.goalMl
    )

    // Map score to Buddy mood
    val buddyMood = WellbeingCalculator.scoreToBuddyMood(wellbeingScore)

    val buddyColor = hexToColor(buddyColorHex)
    val buddyColorTheme = ColorUtil.generateBlobTheme(buddyColor)

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

        Spacer(Modifier.height(16.dp))

        Text(
            text = greeting,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(Modifier.height(2.dp))

        BuddyBlob(
            mood = buddyMood,
            colorTheme = buddyColorTheme,
            modifier = Modifier.size(240.dp)
        )

        Spacer(Modifier.height(2.dp))

        QuoteSection()

        Spacer(Modifier.height(18.dp))

        Text(
            text = "Today's Stats",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )

        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    MoodStatCard(
                        todayMood = todayMood,
                        onClick = onMoodClick
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    WaterStatCard(
                        currentMl = waterState.currentMl,
                        goalMl = waterState.goalMl,
                        onClick = onWaterClick
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SleepStatCard(
                        durationMinutes = sleepState.todaySleep?.durationMinutes,
                        goalMinutes = sleepState.goalMinutes,
                        onClick = onSleepClick
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    HabitsStatCard(
                        done = habits.count { it.completedToday },
                        total = habits.size,
                        onClick = onHabitsClick
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}