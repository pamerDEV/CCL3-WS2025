package com.example.mybuddy.ui.components.health.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.GradientBlueEnd
import com.example.mybuddy.ui.theme.HabitBlue
import com.example.mybuddy.ui.theme.Violet

@Composable
fun HealthTabs(
    selectedTab: HealthTab,
    onTabSelected: (HealthTab) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        HealthTabPill(
            text = "Sleep",
            selected = selectedTab == HealthTab.Sleep,
            activeColor = Violet,
            onClick = { onTabSelected(HealthTab.Sleep) }
        )

        HealthTabPill(
            text = "Water",
            selected = selectedTab == HealthTab.Water,
            activeColor = GradientBlueEnd,
            onClick = { onTabSelected(HealthTab.Water) }
        )
    }
}