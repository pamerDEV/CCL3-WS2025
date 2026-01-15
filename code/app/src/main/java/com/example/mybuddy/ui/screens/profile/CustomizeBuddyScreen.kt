package com.example.mybuddy.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.BlobMood
import com.example.mybuddy.ui.components.BuddyBlob
import com.example.mybuddy.ui.components.profile.ColorWheelPicker
import com.example.mybuddy.ui.components.GradientButton
import com.example.mybuddy.ui.theme.*
import com.example.mybuddy.ui.viewmodel.BuddyViewModel
import com.example.mybuddy.ui.viewmodel.BuddyViewModelFactory
import com.example.mybuddy.utils.ColorUtil
import com.example.mybuddy.utils.hexToColor
import com.example.mybuddy.utils.toHexString

@Composable
fun CustomizeBuddyScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val application = context.applicationContext as MyBuddyApplication
    val viewModel: BuddyViewModel = viewModel(
        factory = BuddyViewModelFactory(application.buddyRepository)
    )

    val savedName by viewModel.buddyName.collectAsState()
    val savedColorHex by viewModel.buddyColorHex.collectAsState()

    var buddyName by remember(savedName) { mutableStateOf(savedName) }
    var selectedColor by remember(savedColorHex) { mutableStateOf(hexToColor(savedColorHex)) }

    val buddyColorTheme = remember(selectedColor) {
        ColorUtil.generateBlobTheme(selectedColor)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar with Back Button
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
        }

        // Title
        Text(
            text = "Customize your Buddy",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Buddy Preview
        BuddyBlob(
            mood = BlobMood.HAPPY,
            colorTheme = buddyColorTheme,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Name & Color Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Name Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose a name",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = buddyName,
                        onValueChange = { buddyName = it },
                        textStyle = MaterialTheme.typography.titleMedium,
                        singleLine = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Violet,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Violet,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Color Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Choose a Colour",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ColorWheelPicker(
                        selectedColor = selectedColor,
                        onColorSelected = { color ->
                            selectedColor = color
                        },
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        GradientButton(
            text = "Save",
            onClick = {
                viewModel.saveBuddyProfile(buddyName, selectedColor.toHexString())
                onSaveClick()
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}