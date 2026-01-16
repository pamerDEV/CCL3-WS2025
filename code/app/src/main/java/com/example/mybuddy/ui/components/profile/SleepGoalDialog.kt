package com.example.mybuddy.ui.components.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SleepGoalDialog(
    currentMinutes: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var value by remember {
        mutableStateOf(currentMinutes / 60f)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSave((value * 60).toInt())
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Sleep goal") },
        text = {
            Column {
                Text("${value.toInt()} hours")
                Slider(
                    value = value,
                    onValueChange = { value = it },
                    valueRange = 4f..12f,
                    steps = 7
                )
            }
        }
    )
}