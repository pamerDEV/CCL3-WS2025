package com.example.mybuddy.ui.screens.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.health.tabs.HealthTabs
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.components.health.tabs.HealthTab
import com.example.mybuddy.ui.viewmodel.WaterViewModel
import com.example.mybuddy.ui.viewmodel.WaterViewModelFactory

@Composable
fun HealthScreen() {

    var selectedTab by remember { mutableStateOf(HealthTab.Sleep) }

    val context = LocalContext.current
    val app = context.applicationContext as MyBuddyApplication

    val waterViewModel: WaterViewModel = viewModel(
        factory = WaterViewModelFactory(app.database.waterLogDao())
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(12.dp))

        HealthTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        Spacer(Modifier.height(32.dp))

        when (selectedTab) {
            HealthTab.Sleep -> {
                SleepTab()
            }

            HealthTab.Water -> {
                WaterTab(waterViewModel)
            }
        }
    }
}
