package com.example.mybuddy.ui.components.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * ColorWheelPicker - HSV Color Wheel for Buddy customization
 *
 * @param selectedColor Current selected color
 * @param onColorSelected Callback when user selects a new color
 * @param modifier Modifier for sizing
 */
@Composable
fun ColorWheelPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier.size(150.dp)
) {
    var selectorPosition by remember { mutableStateOf(Offset.Zero) }
    var centerOffset by remember { mutableStateOf(Offset.Zero) }
    var radius by remember { mutableStateOf(0f) }

    // Initialize selector position from selected color
    LaunchedEffect(selectedColor, radius) {
        if (radius > 0f) {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(selectedColor.toArgb(), hsv)
            val hue = hsv[0]
            val saturation = hsv[1]

            val angle = Math.toRadians(hue.toDouble())
            val dist = saturation * radius

            selectorPosition = Offset(
                x = centerOffset.x + (cos(angle) * dist).toFloat(),
                y = centerOffset.y + (sin(angle) * dist).toFloat()  // ← Geändert: - zu +
            )
        }
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val color = getColorFromPosition(offset, centerOffset, radius)
                        if (color != null) {
                            selectorPosition = offset
                            onColorSelected(color)
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val offset = change.position
                        val color = getColorFromPosition(offset, centerOffset, radius)
                        if (color != null) {
                            selectorPosition = offset
                            onColorSelected(color)
                        }
                    }
                }
        ) {
            val canvasSize = size.minDimension
            radius = canvasSize / 2f
            centerOffset = Offset(size.width / 2f, size.height / 2f)

            // Draw color wheel
            val colors = (0..360 step 1).map { hue ->
                Color.hsv(hue.toFloat(), 1f, 1f)
            }

            // Draw the wheel using sweep gradient
            drawCircle(
                brush = Brush.sweepGradient(colors),
                radius = radius,
                center = centerOffset
            )

            // Draw white-to-transparent radial gradient for saturation
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, Color.Transparent),
                    center = centerOffset,
                    radius = radius
                ),
                radius = radius,
                center = centerOffset
            )

            // Draw selector circle
            if (selectorPosition != Offset.Zero) {
                val colorAtPosition = getColorFromPosition(selectorPosition, centerOffset, radius) ?: selectedColor

                // Outer ring (white)
                drawCircle(
                    color = Color.White,
                    radius = 14.dp.toPx(),
                    center = selectorPosition,
                    style = Stroke(width = 3.dp.toPx())
                )
                // Inner ring (black for contrast)
                drawCircle(
                    color = Color.Black.copy(alpha = 0.3f),
                    radius = 12.dp.toPx(),
                    center = selectorPosition,
                    style = Stroke(width = 1.dp.toPx())
                )
                // Fill with actual color at position
                drawCircle(
                    color = colorAtPosition,
                    radius = 10.dp.toPx(),
                    center = selectorPosition
                )
            }
        }
    }
}

/**
 * Get color from position on the wheel
 */
private fun getColorFromPosition(
    position: Offset,
    center: Offset,
    radius: Float
): Color? {
    val dx = position.x - center.x
    val dy = position.y - center.y
    val distance = sqrt(dx * dx + dy * dy)

    // Check if position is within the wheel
    if (distance > radius) return null

    // Calculate hue from angle (0-360)
    var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble()))  // ← Geändert: -dy zu dy
    if (angle < 0) angle += 360.0
    val hue = angle.toFloat()

    // Calculate saturation from distance (0-1)
    val saturation = (distance / radius).coerceIn(0f, 1f)

    // Value is always 1 for full brightness
    return Color.hsv(hue, saturation, 1f)
}

/**
 * Extension to convert Compose Color to Android ARGB Int
 */
private fun Color.toArgb(): Int {
    return android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}