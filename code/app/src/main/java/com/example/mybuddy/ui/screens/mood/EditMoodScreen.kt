package com.example.mybuddy.ui.screens.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.components.MoodType
import com.example.mybuddy.ui.components.mood.EditMoodItem
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
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Violet)
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🔹 TOP BAR
        item(span = { GridItemSpan(maxLineSpan) }) {
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

                Spacer(Modifier.weight(1f))

                Text(
                    text = DateUtil.formatDate(timestamp),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
        }

        // 🔹 TITLE
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Edit Mood",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 🔹 MOOD GRID
        items(MoodType.entries) { mood ->
            EditMoodItem(
                mood = mood,
                isSelected = mood == selectedMood,
                onClick = { selectedMood = mood }
            )
        }

        // 🔹 NOTE FIELD
        item(span = { GridItemSpan(maxLineSpan) }) {
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
        }

        // 🔹 SAVE BUTTON
        item(span = { GridItemSpan(maxLineSpan) }) {
            GradientButton(
                text = "Save Changes",
                onClick = {
                    if (selectedMood != null) {
                        scope.launch {
                            val mood = viewModel.getMoodById(moodId)
                            mood?.let {
                                viewModel.updateMood(
                                    it.copy(
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
        }

        // 🔹 BOTTOM SPACER (BottomBar safe)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.height(80.dp))
        }
    }
}