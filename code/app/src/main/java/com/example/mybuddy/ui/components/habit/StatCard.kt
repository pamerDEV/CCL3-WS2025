package com.example.mybuddy.ui.components.habit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.Surface
import com.example.mybuddy.ui.theme.TextSecondary

@Composable
fun StatCard(
    label: String, value: String, valueColor: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value, style = MaterialTheme.typography.headlineSmall, color = valueColor
            )
            Text(
                text = label, style = MaterialTheme.typography.labelMedium, color = TextSecondary
            )
        }
    }
}