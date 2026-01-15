package com.example.mybuddy.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mybuddy.ui.components.BottomBar
import com.example.mybuddy.ui.navigation.*

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarHeight = 100.dp

    val showBottomBar =
        Screen.bottomRoutes.any { route ->
            currentRoute == route || currentRoute?.startsWith(route) == true
        }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = if (showBottomBar) {
                        bottomBarHeight
                    } else {
                        bottomBarHeight
                    }
                )
        ) {
            NavGraph(navController)
        }
    }
}
