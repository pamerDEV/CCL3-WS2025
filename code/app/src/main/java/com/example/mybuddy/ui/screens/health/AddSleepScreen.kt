package com.example.mybuddy.ui.screens.health

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.components.SleepQuality
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.SleepViewModel
import com.example.mybuddy.ui.viewmodel.SleepViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSleepScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication

    val viewModel: SleepViewModel = viewModel(
        factory = SleepViewModelFactory(application.sleepRepository)
    )

    val state by viewModel.uiState.collectAsState()
    val isEditing = state.hasLoggedToday

    // Pre-fill
    var selectedBedtimeHour by remember(state.todaySleep) {
        mutableIntStateOf(state.todaySleep?.bedtime?.split(":")?.get(0)?.toIntOrNull() ?: 21)
    }
    var selectedBedtimeMinute by remember(state.todaySleep) {
        mutableIntStateOf(state.todaySleep?.bedtime?.split(":")?.get(1)?.toIntOrNull() ?: 0)
    }
    var selectedWakeHour by remember(state.todaySleep) {
        mutableIntStateOf(state.todaySleep?.wakeTime?.split(":")?.get(0)?.toIntOrNull() ?: 6)
    }
    var selectedWakeMinute by remember(state.todaySleep) {
        mutableIntStateOf(state.todaySleep?.wakeTime?.split(":")?.get(1)?.toIntOrNull() ?: 0)
    }
    var selectedQuality by remember(state.todaySleep) {
        mutableStateOf(
            state.todaySleep?.quality?.let { SleepQuality.fromString(it) } ?: SleepQuality.OKAY
        )
    }

    var showBedtimePicker by remember { mutableStateOf(false) }
    var showWakePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (isEditing) "Edit Sleep" else "Last Night's Sleep",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(48.dp))

        // Bedtime Picker
        Text(
            text = "When did you sleep?",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(Modifier.height(12.dp))

        TimePickerButton(
            hour = selectedBedtimeHour,
            minute = selectedBedtimeMinute,
            onClick = { showBedtimePicker = true }
        )

        Spacer(Modifier.height(32.dp))

        // Wake Time Picker
        Text(
            text = "When did you wake up?",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(Modifier.height(12.dp))

        TimePickerButton(
            hour = selectedWakeHour,
            minute = selectedWakeMinute,
            onClick = { showWakePicker = true }
        )

        Spacer(Modifier.height(32.dp))

        // Sleep Quality
        Text(
            text = "How was your sleep?",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SleepQuality.entries.forEach { quality ->
                SleepQualityOption(
                    quality = quality,
                    isSelected = selectedQuality == quality,
                    onClick = { selectedQuality = quality },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Save Button
        GradientButton(
            text = if (isEditing) "Update" else "Save",
            onClick = {
                val bedtime = String.format("%02d:%02d", selectedBedtimeHour, selectedBedtimeMinute)
                val wakeTime = String.format("%02d:%02d", selectedWakeHour, selectedWakeMinute)

                if (isEditing) {
                    viewModel.updateSleep(bedtime, wakeTime, selectedQuality.name)
                } else {
                    viewModel.addSleep(bedtime, wakeTime, selectedQuality.name)
                }
                onSaveClick()
            }
        )

        Spacer(Modifier.height(24.dp))
    }

    // Bedtime Picker Dialog
    if (showBedtimePicker) {
        TimePickerDialog(
            initialHour = selectedBedtimeHour,
            initialMinute = selectedBedtimeMinute,
            onDismiss = { showBedtimePicker = false },
            onConfirm = { hour, minute ->
                selectedBedtimeHour = hour
                selectedBedtimeMinute = minute
                showBedtimePicker = false
            }
        )
    }

    // Wake Time Picker Dialog
    if (showWakePicker) {
        TimePickerDialog(
            initialHour = selectedWakeHour,
            initialMinute = selectedWakeMinute,
            onDismiss = { showWakePicker = false },
            onConfirm = { hour, minute ->
                selectedWakeHour = hour
                selectedWakeMinute = minute
                showWakePicker = false
            }
        )
    }
}

@Composable
fun TimePickerButton(
    hour: Int,
    minute: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = TextPrimary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = String.format("%02d : %02d", hour, minute),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select time",
                tint = TextSecondary
            )
        }
    }
}

@Composable
fun SleepQualityOption(
    quality: SleepQuality,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Violet else TextPrimary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = quality.iconRes),
            contentDescription = quality.label,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = quality.label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) Violet else TextPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text("OK", color = Violet)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    selectorColor = Violet,
                    timeSelectorSelectedContainerColor = VioletLight.copy(alpha = 0.3f)
                )
            )
        }
    )
}