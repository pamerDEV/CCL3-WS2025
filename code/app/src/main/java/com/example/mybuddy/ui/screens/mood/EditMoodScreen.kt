package com.example.mybuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch

@Composable
fun EditMoodScreen(
    moodId: Long,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication
    val viewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(application.moodRepository)
    )

    val scope = rememberCoroutineScope()

    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var note by remember { mutableStateOf("") }
    var timestamp by remember { mutableLongStateOf(0L) }
    var isLoaded by remember { mutableStateOf(false) }

    // Load existing mood
    LaunchedEffect(moodId) {
        val mood = viewModel.getMoodById(moodId)
        if (mood != null) {
            selectedMood = MoodType.fromString(mood.moodType)
            note = mood.note ?: ""
            timestamp = mood.timestamp
            isLoaded = true
        }
    }

    if (!isLoaded) {
        // Loading
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Violet)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = DateUtil.formatDate(timestamp),
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "Edit Mood",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mood Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(MoodType.entries.toList()) { mood ->
                EditMoodItem(
                    mood = mood,
                    isSelected = mood == selectedMood,
                    onClick = { selectedMood = mood }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Note TextField
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (optional)") },
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

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        GradientButton(
            text = "Save Changes",
            onClick = {
                if (selectedMood != null) {
                    scope.launch {
                        val mood = viewModel.getMoodById(moodId)
                        if (mood != null) {
                            viewModel.updateMood(
                                mood.copy(
                                    moodType = selectedMood!!.name,
                                    note = note.ifBlank { null }
                                )
                            )
                        }
                        onSaveClick()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun EditMoodItem(
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
            modifier = Modifier.size(56.dp),
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