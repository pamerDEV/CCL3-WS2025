package com.example.mybuddy.ui.screens.mood

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.example.mybuddy.db.entity.MoodEntity
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.components.MoodType
import com.example.mybuddy.ui.components.mood.MoodStatsRow
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.MoodViewModel
import com.example.mybuddy.ui.viewmodel.MoodViewModelFactory
import com.example.mybuddy.utils.DateUtil
import java.util.*

@Composable
fun MoodScreen(
    onAddMoodClick: () -> Unit = {},
    onDayClick: (Long, Boolean) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication
    val viewModel: MoodViewModel = viewModel(
        factory = MoodViewModelFactory(application.moodRepository)
    )

    val allMoods by viewModel.allMoods.collectAsState()

    var currentYear by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var currentMonth by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp) // Extra bottom padding for button
        ) {
            Text(
                text = "Mood History",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (currentMonth == 0) {
                        currentMonth = 11
                        currentYear--
                    } else {
                        currentMonth--
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous month",
                        tint = Violet
                    )
                }

                Text(
                    text = DateUtil.formatMonthYear(
                        Calendar.getInstance().apply {
                            set(currentYear, currentMonth, 1)
                        }.timeInMillis
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )

                IconButton(onClick = {
                    if (currentMonth == 11) {
                        currentMonth = 0
                        currentYear++
                    } else {
                        currentMonth++
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next month",
                        tint = Violet
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            MoodCalendar(
                year = currentYear,
                month = currentMonth,
                moods = allMoods,
                onDayClick = onDayClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Row
            MoodStatsRow(
                moods = allMoods,
                year = currentYear,
                month = currentMonth
            )
        }

        // Floating Button at bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Background)
                .padding(16.dp)
        ) {
            GradientButton(
                text = "Add Today's Mood",
                onClick = onAddMoodClick
            )
        }
    }
}

@Composable
fun MoodCalendar(
    year: Int,
    month: Int,
    moods: List<MoodEntity>,
    onDayClick: (Long, Boolean) -> Unit
) {
    val daysInMonth = DateUtil.getDaysInMonth(year, month)
    val firstDayOfWeek = DateUtil.getFirstDayOfWeek(year, month)

    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var dayCounter = 1
        // firstDayOfWeek for Monday start (0 = Monday)
        val adjustedFirstDay = if (firstDayOfWeek == 0) 6 else firstDayOfWeek - 1

        for (week in 0..5) {
            if (dayCounter > daysInMonth) break

            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayOfWeek in 0..6) {
                    if (week == 0 && dayOfWeek < adjustedFirstDay || dayCounter > daysInMonth) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val day = dayCounter
                        val calendar = Calendar.getInstance().apply {
                            set(year, month, day, 12, 0, 0)
                        }
                        val dayTimestamp = calendar.timeInMillis

                        val moodForDay = moods.find { mood ->
                            DateUtil.isSameDay(mood.timestamp, dayTimestamp)
                        }

                        CalendarDay(
                            day = day,
                            mood = moodForDay,
                            isToday = DateUtil.isToday(dayTimestamp),
                            onClick = {
                                onDayClick(dayTimestamp, moodForDay != null)
                            },
                            modifier = Modifier.weight(1f)
                        )

                        dayCounter++
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun CalendarDay(
    day: Int,
    mood: MoodEntity?,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isToday) VioletLight.copy(alpha = 0.3f) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (mood != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val moodType = MoodType.fromString(mood.moodType)
                Image(
                    painter = painterResource(id = moodType.iconRes),
                    contentDescription = moodType.label,
                    modifier = Modifier.size(34.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = day.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isToday) Violet else TextSecondary
                )
            }
        } else {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isToday) Violet else TextSecondary
            )
        }
    }
}