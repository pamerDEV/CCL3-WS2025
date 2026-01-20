package com.example.mybuddy.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.BlobMood
import com.example.mybuddy.ui.components.BottomBar
import com.example.mybuddy.ui.components.BuddyBlob
import com.example.mybuddy.ui.navigation.*
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.theme.BlobThemes
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.viewmodel.OnboardingViewModel
import com.example.mybuddy.ui.viewmodel.OnboardingViewModelFactory

@Composable
fun MainScreen(
    app: MyBuddyApplication
) {
    val navController = rememberNavController()

    val onboardingViewModel: OnboardingViewModel = viewModel(
        factory = OnboardingViewModelFactory(app.userSettingsRepository)
    )

    val onboardingDone by onboardingViewModel.onboardingDone.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Content
        if (onboardingDone == true) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Scaffold(
                bottomBar = {
                    val showBottomBar = Screen.bottomRoutes.any { route ->
                        currentRoute == route || currentRoute?.startsWith("health") == true
                    }
                    if (showBottomBar) {
                        BottomBar(navController)
                    }
                }
            ) { padding ->
                NavGraph(
                    navController = navController,
                    modifier = Modifier.padding(padding)
                )
            }
        } else if (onboardingDone == false) {
            OnboardingScreen(
                onCommitClick = { onboardingViewModel.completeOnboarding() }
            )
        }

        // Splash mit Fade-Out
        AnimatedVisibility(
            visible = onboardingDone == null,
            exit = fadeOut(animationSpec = tween(400))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BuddyBlob(
                        mood = BlobMood.HAPPY,
                        colorTheme = BlobThemes.purple,
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "MyBuddy",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}
