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
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.components.health.tabs.HealthTabs
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.components.health.tabs.HealthTab

@Composable
fun HealthScreen() {

    var selectedTab by remember { mutableStateOf(HealthTab.Sleep) }

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
                WaterTab()
            }
        }
    }
}
