package com.example.mybuddy.ui.components.health.water

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.HabitBlue
import com.example.mybuddy.ui.theme.TextSecondary

@Composable
fun WeeklyCircle(
    percent: Int,
    day: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(44.dp)
        ) {
            CircularProgressIndicator(
                progress = { percent / 100f },
                color = HabitBlue,
                strokeWidth = 4.dp,
                trackColor = HabitBlue.copy(alpha = 0.15f),
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                gapSize = (-15).dp
            )

            Text(
                text = "$percent%",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = day,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}