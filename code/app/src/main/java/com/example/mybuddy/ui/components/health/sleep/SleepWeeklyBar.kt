package com.example.mybuddy.ui.components.health.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary
import com.example.mybuddy.ui.theme.VioletLight
import com.example.mybuddy.utils.SleepQualityColor

@Composable
fun SleepWeeklyBar(
    durationHours: Float,
    goalHours: Float,
    day: String,
    quality: String?
) {
    val hasData = durationHours > 0
    val barColor = if (hasData) {
        SleepQualityColor.getColor(quality)
    } else {
        VioletLight.copy(alpha = 0.3f)
    }

    // Höhe relativ zum Goal berechnen
    val maxHeight = 100.dp
    val progress = if (hasData && goalHours > 0) {
        (durationHours / goalHours).coerceIn(0f, 1f)
    } else {
        0.1f  // Minimale Höhe wenn keine Daten
    }
    val barHeight = maxHeight * progress

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Stunden Label oben
        Text(
            text = if (hasData) "${durationHours.toInt()}h" else "-",
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary
        )

        Spacer(Modifier.height(4.dp))

        // Bar Container
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(maxHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Background Track
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(maxHeight)
                    .clip(RoundedCornerShape(14.dp))
                    .background(VioletLight.copy(alpha = 0.2f))
            )

            // Filled Bar
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(barHeight)
                    .clip(RoundedCornerShape(14.dp))
                    .background(barColor)
            )
        }

        Spacer(Modifier.height(8.dp))

        // Day Label
        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}