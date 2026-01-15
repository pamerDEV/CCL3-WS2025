package com.example.mybuddy.ui.components.health.water

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybuddy.R
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.theme.GradientBlueEnd
import com.example.mybuddy.ui.theme.GradientBlueStart
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary

@Composable
fun AddWaterDialog(
    onDismiss: () -> Unit,
    onAdd: (Int) -> Unit,
    dailyGoal: Int = 2000  // Parameter für Daily Goal
) {
    var amount by remember { mutableFloatStateOf((dailyGoal / 8f)) }

    // Dynamisch: 4 Gläser = Daily Goal
    val mlPerGlass = dailyGoal / 4
    val glasses = (amount / mlPerGlass).toInt().coerceIn(0, 4)
    val maxSlider = dailyGoal.toFloat()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Background,
        modifier = Modifier.border(
            width = 1.dp,
            color = TextPrimary.copy(alpha = 0.25f),
            shape = RoundedCornerShape(20.dp)
        ),
        title = {
            Text(
                text = "Quick Add",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Amount display
                Text(
                    text = "${amount.toInt()} ml",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = GradientBlueEnd
                )

                Spacer(Modifier.height(16.dp))

                // Glass indicators - clickable
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 1..4) {
                        Icon(
                            painter = painterResource(id = R.drawable.water_glass_icon),
                            contentDescription = "Glass $i",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { amount = (i * mlPerGlass).toFloat() },
                            tint = if (i <= glasses) GradientBlueEnd else TextSecondary.copy(alpha = 0.3f)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Slider
                Slider(
                    value = amount,
                    onValueChange = { amount = it },
                    valueRange = 50f..maxSlider,
                    colors = SliderDefaults.colors(
                        thumbColor = GradientBlueEnd,
                        activeTrackColor = GradientBlueEnd,
                        inactiveTrackColor = GradientBlueEnd.copy(alpha = 0.3f)
                    )
                )

                // Min/Max labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "50ml",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "${dailyGoal}ml",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GradientBlueStart, GradientBlueEnd)
                        )
                    )
                    .clickable { onAdd(amount.toInt()) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    )
}