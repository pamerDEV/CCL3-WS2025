package com.example.mybuddy.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.components.MoodType
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary

@Composable
fun MoodStatCard (
    todayMood: MoodType?,
    onClick: () -> Unit
) {
    Card (
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Background),
        modifier = Modifier
            .height(140.dp)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = TextPrimary.copy(alpha = 0.25f),
                shape = RoundedCornerShape(20.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Today's Mood",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )

            if (todayMood != null) {
                // Mood exists
                Image(
                    painter = painterResource(id = todayMood.iconRes),
                    contentDescription = todayMood.label,
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = todayMood.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            } else {
                /// Layout for no mood: Use a Box with weight to center the prompt
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "How do you feel today?",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}