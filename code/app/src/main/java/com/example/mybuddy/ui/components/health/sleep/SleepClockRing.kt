package com.example.mybuddy.ui.components.health.sleep

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.Violet
import com.example.mybuddy.ui.theme.VioletLight
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SleepClockRing(
    durationMinutes: Int,
    bedtime: String,
    wakeTime: String,
    modifier: Modifier = Modifier
) {
    val hours = durationMinutes / 60
    val minutes = durationMinutes % 60

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(220.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 12.dp.toPx()
            val outerRadius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)
            val innerRadius = outerRadius - strokeWidth - 4.dp.toPx()

            // Inner filled circle (light violet)
            drawCircle(
                color = VioletLight.copy(alpha = 0.2f),
                radius = innerRadius,
                center = center
            )

            // Background ring
            drawCircle(
                color = VioletLight.copy(alpha = 0.3f),
                radius = outerRadius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Calculate angles from times
            val bedAngle = timeToAngle(bedtime)
            val wakeAngle = timeToAngle(wakeTime)
            val sweepAngle = calculateSweepAngle(bedAngle, wakeAngle)

            // Progress arc
            drawArc(
                color = Violet,
                startAngle = bedAngle - 90,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Clock numbers
            val numberRadius = innerRadius - 12.dp.toPx()
            for (i in 1..12) {
                val angle = Math.toRadians((i * 30 - 90).toDouble())
                val x = center.x + numberRadius * cos(angle).toFloat()
                val y = center.y + numberRadius * sin(angle).toFloat()

                drawContext.canvas.nativeCanvas.drawText(
                    i.toString(),
                    x,
                    y + 5.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.parseColor("#666666")
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }

        // Center text
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)) {
                    append("$hours")
                }
                withStyle(SpanStyle(fontSize = 18.sp)) {
                    append("h")
                }
                withStyle(SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)) {
                    append("$minutes")
                }
                withStyle(SpanStyle(fontSize = 18.sp)) {
                    append("m")
                }
            },
            color = TextPrimary
        )
    }
}

private fun timeToAngle(time: String): Float {
    val (hours, minutes) = time.split(":").map { it.toInt() }
    val totalMinutes = (hours % 12) * 60 + minutes
    return (totalMinutes / (12f * 60f)) * 360f
}

private fun calculateSweepAngle(startAngle: Float, endAngle: Float): Float {
    return if (endAngle >= startAngle) {
        endAngle - startAngle
    } else {
        360f - startAngle + endAngle
    }
}