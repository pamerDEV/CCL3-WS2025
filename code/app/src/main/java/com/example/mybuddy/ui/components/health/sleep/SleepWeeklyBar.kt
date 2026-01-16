package com.example.mybuddy.ui.components.health.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
    val hasData = durationHours > 0 && goalHours > 0

    val rawProgress =
        if (hasData) (durationHours / goalHours).coerceIn(0f, 1f) else 0f

    val displayProgress = when {
        !hasData -> 0f
        rawProgress < 0.1f -> 0.1f
        else -> rawProgress
    }

    val barColor = SleepQualityColor.getColor(quality)
    val maxHeight = 100.dp
    val barHeight = maxHeight * displayProgress

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (hasData) "${durationHours.toInt()}h" else "-",
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary
        )

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(28.dp)
                .height(maxHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(VioletLight.copy(alpha = 0.2f))
            )
            if (displayProgress > 0f) {
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(barHeight)
                        .clip(RoundedCornerShape(14.dp))
                        .background(barColor)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}