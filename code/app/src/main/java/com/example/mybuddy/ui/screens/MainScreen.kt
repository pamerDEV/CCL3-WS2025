package com.example.mybuddy.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mybuddy.ui.components.BottomBar
import com.example.mybuddy.ui.navigation.*

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
