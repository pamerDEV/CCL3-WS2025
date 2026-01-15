package com.example.mybuddy.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mybuddy.ui.navigation.Screen
import com.example.mybuddy.ui.theme.Background
import com.example.mybuddy.ui.theme.TextPrimary
import com.example.mybuddy.ui.theme.TextSecondary
import com.example.mybuddy.ui.theme.Violet

@Composable
fun BottomBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        NavigationBar(
            containerColor = Background,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = TextPrimary.copy(alpha = 0.15f),
                    shape = RectangleShape
                )
        ) {
            Screen.bottomItems.forEach { screen ->

                var currentScreen = currentRoute

                if (currentScreen.toString().startsWith("health")) {
                    currentScreen = "health"
                }

                val selected = currentScreen == screen.route

                NavigationBarItem(
                    selected = selected,
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Violet,
                        selectedTextColor = Violet,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
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
}
