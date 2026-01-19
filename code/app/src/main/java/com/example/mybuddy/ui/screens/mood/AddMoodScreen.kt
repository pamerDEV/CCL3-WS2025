package com.example.mybuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.components.MoodType
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.MoodViewModel
import com.example.mybuddy.ui.viewmodel.MoodViewModelFactory
import com.example.mybuddy.utils.DateUtil

@Composable
fun AddMoodScreen(
    timestamp: Long? = null,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication
    val viewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(application.moodRepository)
    )

    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var note by remember { mutableStateOf("") }
    var step by remember { mutableIntStateOf(1) }

    val moodTimestamp = timestamp ?: System.currentTimeMillis()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (step == 2) step = 1 else onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (!DateUtil.isToday(moodTimestamp)) {
                Text(
                    text = DateUtil.formatDate(moodTimestamp),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Step $step of 2",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (step == 1) {
            MoodSelectionStep(
                selectedMood = selectedMood,
                onMoodSelected = { selectedMood = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GradientButton(
                text = "Next",
                onClick = { step = 2 },
                enabled = selectedMood != null
            )
        } else {
            AddNoteStep(
                selectedMood = selectedMood!!,
                note = note,
                onNoteChange = { note = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            GradientButton(
                text = "Save",
                onClick = {
                    viewModel.addMoodWithTimestamp(
                        moodType = selectedMood!!.name,
                        note = note.ifBlank { null },
                        timestamp = moodTimestamp
                    )
                    onSaveClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun MoodSelectionStep(
    selectedMood: MoodType?,
    onMoodSelected: (MoodType) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 360.dp)
        ) {
            items(MoodType.entries.toList()) { mood ->
                MoodItem(
                    mood = mood,
                    isSelected = mood == selectedMood,
                    onClick = { onMoodSelected(mood) }
                )
            }
        }
    }
}

@Composable
fun MoodItem(
    mood: MoodType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) VioletLight.copy(alpha = 0.3f) else Color.Transparent)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = mood.iconRes),
            contentDescription = mood.label,
            modifier = Modifier.size(84.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = mood.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) Violet else TextSecondary
        )
    }
}

@Composable
fun AddNoteStep(
    selectedMood: MoodType,
    note: String,
    onNoteChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Add a note (optional)",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text("What's on your mind?") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Violet,
                unfocusedBorderColor = TextSecondary,
                cursorColor = Violet
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
