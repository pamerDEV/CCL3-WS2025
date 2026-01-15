package com.example.mybuddy.ui.components.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary

@Composable
fun QuoteBubble(
    text: String
) {
    val borderColor = TextPrimary.copy(alpha = 0.2f)
    val bubbleColor = Background

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val triangleSize = 16.dp.toPx()
                val triangleX = size.width - 60.dp.toPx()
                val cornerRadius = 16.dp.toPx()
                val bubbleTop = triangleSize
                val bubbleHeight = size.height - triangleSize

                val path = Path().apply {
                    moveTo(cornerRadius, bubbleTop)
                    lineTo(triangleX - triangleSize / 2, bubbleTop)
                    lineTo(triangleX, 0f)
                    lineTo(triangleX + triangleSize / 2, bubbleTop)
                    lineTo(size.width - cornerRadius, bubbleTop)
                    quadraticTo(size.width, bubbleTop, size.width, bubbleTop + cornerRadius)
                    lineTo(size.width, bubbleTop + bubbleHeight - cornerRadius)
                    quadraticTo(size.width, bubbleTop + bubbleHeight, size.width - cornerRadius, bubbleTop + bubbleHeight)
                    lineTo(cornerRadius, bubbleTop + bubbleHeight)
                    quadraticTo(0f, bubbleTop + bubbleHeight, 0f, bubbleTop + bubbleHeight - cornerRadius)
                    lineTo(0f, bubbleTop + cornerRadius)
                    quadraticTo(0f, bubbleTop, cornerRadius, bubbleTop)
                    close()
                }

                drawPath(path = path, color = bubbleColor)
                drawPath(path = path, color = borderColor, style = Stroke(width = 1.dp.toPx()))
            }
    ) {
        // Text Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daily Quote",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "\"$text\"",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}