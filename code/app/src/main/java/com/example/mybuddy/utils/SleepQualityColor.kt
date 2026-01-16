package com.example.mybuddy.utils

import androidx.compose.ui.graphics.Color
import com.example.mybuddy.ui.theme.SleepAwesome
import com.example.mybuddy.ui.theme.SleepOkay
import com.example.mybuddy.ui.theme.SleepTerrible

object SleepQualityColor {

    fun getColor(quality: String?): Color {
        return when (quality?.uppercase()) {
            "AWESOME" -> SleepAwesome
            "OKAY" -> SleepOkay
            "TERRIBLE" -> SleepTerrible
            else -> SleepOkay // Default
        }
    }

    fun getTrackColor(quality: String?): Color {
        return getColor(quality).copy(alpha = 0.2f)
    }
}