package com.example.mybuddy.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mybuddy.ui.navigation.Screen

@Composable
fun BottomBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        Screen.bottomItems.forEach { screen ->

            val selected = currentRoute == screen.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(screen.iconRes),
                        contentDescription = screen.label
                    )
                },
                label = {
                    Text(screen.label)
                }
            )
        }
    }
}
