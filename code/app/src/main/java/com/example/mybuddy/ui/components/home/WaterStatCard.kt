package com.example.mybuddy.ui.components.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.theme.GradientBlueEnd
import com.example.mybuddy.ui.theme.GradientBlueStart
import com.example.mybuddy.ui.theme.TextPrimary

@Composable
fun WaterStatCard(
    currentMl: Int,
    goalMl: Int,
    onClick: () -> Unit
) {
    val progress =
        if (goalMl == 0) 0f else currentMl.toFloat() / goalMl.toFloat()

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
                text = "Water",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )

            // Ring with inner circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(64.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val strokeWidth = 6.dp.toPx()
                    val outerRadius = (size.minDimension - strokeWidth) / 2
                    val center = Offset(size.width / 2, size.height / 2)
                    val innerRadius = outerRadius - strokeWidth - 2.dp.toPx()

                    // Inner filled circle (light blue)
                    drawCircle(
                        color = GradientBlueStart,
                        radius = innerRadius,
                        center = center
                    )

                    // Background ring
                    drawCircle(
                        color = GradientBlueStart,
                        radius = outerRadius,
                        center = center,
                        style = Stroke(width = strokeWidth)
                    )

                    // Progress arc
                    drawArc(
                        color = GradientBlueEnd,
                        startAngle = -90f,
                        sweepAngle = 360f * progress.coerceIn(0f, 1f),
                        useCenter = false,
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }

            // Goal text
            Text(
                text = "${currentMl}/${goalMl}ml",
                style = MaterialTheme.typography.bodySmall,
                color = GradientBlueEnd
            )
        }
    }
}