package com.example.mybuddy.ui.components.health.water

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.GradientBlueEnd
import com.example.mybuddy.ui.theme.GradientBlueStart
import com.example.mybuddy.ui.theme.HabitBlue
import com.example.mybuddy.ui.theme.TextPrimary

@Composable
fun WaterProgressRing(
    progress: Float
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(220.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 12.dp.toPx()
            val outerRadius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)
            val innerRadius = outerRadius - strokeWidth - 4.dp.toPx()

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
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary
        )
    }
}