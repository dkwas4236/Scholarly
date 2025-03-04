package com.example.scholarly.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.ui.theme.Purple40


@Composable
fun BottomStudentBar(navController: NavController, bottomBarState: MutableState<Boolean>) {
    // this line tracks the currently selected item in the bottom bar
    val selectedItem = when (navController.currentBackStackEntry?.destination?.route) {
        "student" -> 0
        "settingsstudent" -> 1
        else -> 0
    }

    // List of items for the bottom navigation bar
    val items = listOf("Home", "Settings")


    // Animated visibility for the bottom bar allowing for a smoother transition
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        // Navigation bar to hold the items with custom colors
        NavigationBar(
            containerColor = MainActivity.backgroundPurple,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {

            // iterates through the items and create a NavigationBarItem for each one.
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        // set the icon tint based on whether the item is selected.
                        val iconTint = if (selectedItem == index) MainActivity.ColorStorage.iconPurple else Color.White

                        // display appropriate icons for each item in the navigation bar
                        when (item) {
                            "Home" -> Icon(Icons.Default.Home, contentDescription = item, tint = iconTint)
                            "Settings" -> Icon(Icons.Filled.Settings, contentDescription = item, tint = iconTint)
                        }
                    },

                    // label  each item in the navigation bar with white color
                    label = { Text(
                        item,
                        color = Color.White
                    )},
                    selected = selectedItem == index,
                    // handle item click, updates the selected item and navigating to the selected page.
                    onClick = {
                        when (index) {
                            0 -> navController.navigate("student")
                            1 -> navController.navigate("settingsstudent")
                        }
                    }
                )
            }
        }
    }
}
