package com.example.mybuddy.ui.components.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary
import com.example.mybuddy.ui.theme.Violet
import com.example.mybuddy.ui.theme.VioletLight

@Composable
fun SleepStatCard(
    durationMinutes: Int?,
    goalMinutes: Int,
    onClick: () -> Unit
) {
    val progress = if (durationMinutes != null && goalMinutes > 0) {
        (durationMinutes.toFloat() / goalMinutes).coerceIn(0f, 1f)
    } else 0f

    Card(
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
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sleep",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )

            if (durationMinutes != null) {
                // Sleep logged - show ring with duration
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(70.dp)
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val strokeWidth = 6.dp.toPx()
                        val radius = (size.minDimension - strokeWidth) / 2

                        // Background circle
                        drawCircle(
                            color = VioletLight.copy(alpha = 0.3f),
                            radius = radius,
                            style = Stroke(width = strokeWidth)
                        )

                        // Progress arc
                        drawArc(
                            color = Violet,
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                            size = Size(size.width - strokeWidth, size.height - strokeWidth)
                        )
                    }

                    // Duration text
                    val hours = durationMinutes / 60
                    val minutes = durationMinutes % 60
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                                append("$hours")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("h")
                            }
                            withStyle(SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)) {
                                append("$minutes")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("m")
                            }
                        },
                        color = TextPrimary
                    )
                }

                // Goal text
                Text(
                    text = "Goal: ${goalMinutes / 60}h",
                    style = MaterialTheme.typography.bodySmall,
                    color = Violet
                )
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Log your sleep!",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

        }
    }
}