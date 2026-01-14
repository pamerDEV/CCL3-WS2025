package com.example.mybuddy.ui.components.health.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.TextSecondary

@Composable
fun HealthTabPill(
    text: String,
    selected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (selected) activeColor.copy(alpha = 0.15f)
        else Color.Transparent

    val textColor =
        if (selected) activeColor
        else TextSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}