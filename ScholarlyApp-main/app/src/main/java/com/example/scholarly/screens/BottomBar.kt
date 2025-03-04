package com.example.scholarly.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import androidx.compose.ui.Modifier


@Composable
fun BottomBar(navController: NavController, bottomBarState: MutableState<Boolean>) {
    // this line tracks the currently selected item in the bottom bar
    val selectedItem = when (navController.currentBackStackEntry?.destination?.route) {
        "users" -> 0
        "meetings" -> 1
        "approvals" -> 2
        "settings" -> 3
        else -> 0
        }

    // List of items for the bottom navigation bar
    val items = listOf("Users", "Meetings","Approvals", "Settings")


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
                            "Users" -> Icon(Icons.Default.Person, contentDescription = item, tint = iconTint)
                            "Meetings" -> Icon(Icons.Default.DateRange, contentDescription = item, tint = iconTint)
                            "Approvals" -> Icon(Icons.Default.Check, contentDescription = item, tint = iconTint)
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
                            0 -> navController.navigate("users")
                            1 -> navController.navigate("meetings")
                            2 -> navController.navigate("approvals")
                            3 -> navController.navigate("settings")
                        }
                    }
                )
            }
        }
    }
}
