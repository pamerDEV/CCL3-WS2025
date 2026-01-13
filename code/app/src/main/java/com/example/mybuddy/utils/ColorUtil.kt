package com.example.mybuddy.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils

/**
 * generates 4 color shading from the basis color
 * for the BudddyBlob gradient
 */
object ColorUtil {

    fun generateBlobTheme(baseColor: Color): List<Color> {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(baseColor.toArgb(), hsl)

        // Light
        val light = hsl.copyOf().apply { this[2] = (this[2] + 0.25f).coerceAtMost(0.9f) }

        // Mid
        val mid = hsl.copyOf().apply { this[2] = (this[2] + 0.1f).coerceAtMost(0.8f) }

        // MidDark
        val midDark = hsl.copyOf()

        // Dark
        val dark = hsl.copyOf().apply { this[2] = (this[2] - 0.15f).coerceAtLeast(0.2f) }

        return listOf(
            Color(ColorUtils.HSLToColor(light)),
            Color(ColorUtils.HSLToColor(mid)),
            Color(ColorUtils.HSLToColor(midDark)),
            Color(ColorUtils.HSLToColor(dark))
        )
    }

    private fun Color.toArgb(): Int {
        return android.graphics.Color.argb(
            (alpha * 255).toInt(),
            (red * 255).toInt(),
            (green * 255).toInt(),
            (blue * 255).toInt()
        )
    }
}