package com.example.mybuddy.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mybuddy.MyBuddyApplication
import com.example.mybuddy.ui.components.BottomBar
import com.example.mybuddy.ui.navigation.*
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

    if (!onboardingDone) {
        OnboardingScreen(
            onCommitClick = {
                onboardingViewModel.completeOnboarding()
            }
        )
        return
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val showBottomBar =
                Screen.bottomRoutes.any { route ->
                    currentRoute == route ||
                            currentRoute?.startsWith("health") == true
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
}

