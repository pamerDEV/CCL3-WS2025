package com.example.mybuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.mybuddy.db.entity.MoodEntity
import com.example.mybuddy.ui.components.MoodType
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.MoodViewModel
import com.example.mybuddy.ui.viewmodel.MoodViewModelFactory
import com.example.mybuddy.utils.DateUtil

@Composable
fun MoodDayView(
    timestamp: Long,
    onBackClick: () -> Unit = {},
    onEditClick: (Long) -> Unit = {},      // Für existierenden Mood (moodId)
    onAddClick: (Long) -> Unit = {},       // Für neuen Mood (timestamp)
    onDeleteSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication
    val viewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(application.moodRepository)
    )

    val allMoods by viewModel.allMoods.collectAsState()

    val moodForDay = allMoods.find { mood ->
        DateUtil.isSameDay(mood.timestamp, timestamp)
    }

    // Nur vergangene Tage oder heute können bearbeitet werden
    val canEdit = timestamp <= System.currentTimeMillis()

    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
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

            if (moodForDay != null) {
                // Hat Mood -> Edit + Delete
                IconButton(onClick = { onEditClick(moodForDay.id) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Violet
                    )
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = DeleteRed
                    )
                }
            } else if (canEdit) {
                // Kein Mood + Vergangenheit/Heute -> Add Button
                IconButton(onClick = { onAddClick(timestamp) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Add Mood",
                        tint = Violet
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date
        Text(
            text = DateUtil.formatDate(timestamp),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (moodForDay != null) {
            MoodContent(mood = moodForDay)
        } else {
            NoMoodContent()
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && moodForDay != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Mood") },
            text = { Text("Are you sure you want to delete this mood entry?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteMood(moodForDay)
                        showDeleteDialog = false
                        onDeleteSuccess()
                    }
                ) {
                    Text("Delete", color = DeleteRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun MoodContent(mood: MoodEntity) {
    val moodType = MoodType.fromString(mood.moodType)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = moodType.iconRes),
            contentDescription = moodType.label,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = moodType.label,
            style = MaterialTheme.typography.headlineMedium,
            color = Violet
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = DateUtil.formatDate(mood.timestamp, "HH:mm"),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        if (!mood.note.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Note",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mood.note,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun NoMoodContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "No mood logged",
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )

        Text(
            text = "for this day",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}