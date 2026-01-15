package com.example.mybuddy.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Violet (Buttons, NavBar Selection)
val Violet = Color(0xFF9756F2)
val VioletLight = Color(0xFFDAB5FF)
val VioletDark = Color(0xFF8A38F5)

// Violet Gradient for Buttons (z.B. "Add Today's Mood")
val GradientVioletStart = Color(0xFFDAB5FF)
val GradientVioletEnd = Color(0xFF9756F2)

// blue Gradient für Water Tab Button
val GradientBlueStart = Color(0xFFE9F1FF)
val GradientBlueEnd = Color(0xFF60CBFF)

// Delete Button Rot
val DeleteRed = Color(0xFFC60F12)               // Delete Button Text
val DeleteRedLight = Color(0xFFFFE6E7)          // Delete Button Background

// Text Colors
val TextPrimary = Color(0xFF0F172A)      // main text (black)
val TextSecondary = Color(0xFF959595)    // Secondary text (grey)
val TextViolet = Color(0xFF8A38F5)       // Violet Text

// Background & Surface
val Background = Color(0xFFFFFFFF)
val Surface = Color(0xFFF8FAFC)         // Cards, Dialogs

// Habit Color Picker (Rainbow)
val HabitRed = Color(0xFFEF4444)
val HabitOrange = Color(0xFFF97316)
val HabitYellow = Color(0xFFEAB308)
val HabitGreen = Color(0xFF22C55E)
val HabitBlue = Color(0xFF3B82F6)
val HabitIndigo = Color(0xFF6366F1)
val HabitPink = Color(0xFFEC4899)
val HabitTeal = Color(0xFF14B8A6)

//virtual companion - Buddy
val EyeColor = Color(0xFF1A1A2E)
val CheekPink = Color(0xFFFD99A8)
val SparkleYellow = Color(0xFFFFE66D)
val SparkleCyan = Color(0xFF7DD3FC)

object BlobThemes {
    // Purple (Default)
    val purple = listOf(
        Color(0xFFDAB5FF),  // Light
        Color(0xFFB87AFF),  // Mid
        Color(0xFF9756F2),  // MidDark
        Color(0xFF7B3FD3)   // Dark
    )
}

// Alte Farben (für Theme.kt Kompatibilität)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)