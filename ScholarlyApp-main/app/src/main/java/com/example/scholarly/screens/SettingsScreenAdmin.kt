package com.example.scholarly.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.viewmodel.UserViewModel

@Composable
fun SettingsScreen(navController: NavController, userViewModel: UserViewModel, bottomBarState: MutableState<Boolean>) {

    val databaseHelper = remember { DatabaseHelper(navController.context) }
    val showDbDialog = remember { mutableStateOf(false) }

    if (showDbDialog.value) {
        AlertDialog(
            onDismissRequest = { showDbDialog.value = false },
            title = { Text("Clear Database") },
            text = { Text("Are you sure you want to clear all data? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            databaseHelper.clearAllTables()
                            Toast.makeText(navController.context, "Database cleared successfully", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(navController.context, "Error clearing database: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                        showDbDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDbDialog.value = false },
                    colors = ButtonDefaults.buttonColors(MainActivity.ColorStorage.backgroundPurpleAlt)
                ) {
                    Text("Cancel", color = MainActivity.ColorStorage.textPurple)
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BottomBar(navController, bottomBarState)
            }
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MainActivity.ColorStorage.backgroundPurple
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Settings", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                Spacer(modifier = Modifier.height(20.dp))

                ElevatedButton(onClick = {
                    val currentUserId = userViewModel.getCurrentUserId()

                    if (currentUserId != null) {
                        val currentUser = databaseHelper.findTutorById(currentUserId)
                        navController.navigate("userDetails/${currentUserId}")
                    } else {
                        Toast.makeText(navController.context, "User not logged in", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Account Information", color = MainActivity.ColorStorage.textPurple)
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(onClick = {
                    Toast.makeText(navController.context, "Deactivate Account Clicked", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Deactivate Account", color = MainActivity.ColorStorage.textPurple)
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(onClick = {
                    Toast.makeText(navController.context, "Contact Us Clicked", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Contact Us", color = MainActivity.ColorStorage.textPurple)
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(onClick = {
                    userViewModel.setCurrentUserId(null)
                    Toast.makeText(navController.context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                }) {
                    Text("Log Out", color = MainActivity.ColorStorage.textPurple)
                }

                Spacer(modifier = Modifier.height(30.dp))

                ElevatedButton(
                    onClick = { showDbDialog.value = true },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Clear Database", color = Color.White)
                }
            }
        }
    }
}
