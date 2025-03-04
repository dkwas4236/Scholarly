package com.example.scholarly.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.ui.theme.darkGrayCustom
import com.example.scholarly.viewmodel.UserViewModel
import androidx.compose.ui.text.AnnotatedString

@Composable
fun SettingsScreenTutor(
    navController: NavController,
    userViewModel: UserViewModel,
    bottomBarState: MutableState<Boolean>
) {

    val databaseHelper = remember { DatabaseHelper(navController.context) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    // State to manage the visibility of the Contact Us dialog
    var showContactUsDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BottomTutorBar(navController, bottomBarState)
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
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(20.dp))

                ElevatedButton(onClick = {
                    val currentUserId = userViewModel.getCurrentUserId()

                    if (currentUserId != null) {
                        val currentUser = databaseHelper.findTutorById(currentUserId)
                        navController.navigate("userDetails/${currentUserId}")
                    } else {
                        Toast.makeText(
                            navController.context,
                            "User not logged in",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    Text(
                        "Tutor Account Information",
                        color = MainActivity.ColorStorage.textPurple
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(onClick = {
                    Toast.makeText(
                        navController.context,
                        "Deactivate Account Clicked",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text(
                        "Deactivate Account",
                        color = MainActivity.ColorStorage.textPurple
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(onClick = {
                    // Show the Contact Us dialog
                    showContactUsDialog = true
                }) {
                    Text(
                        "Contact Us",
                        color = MainActivity.ColorStorage.textPurple
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(onClick = {
                    userViewModel.setCurrentUserId(null)
                    Toast.makeText(
                        navController.context,
                        "Logged out successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("login")
                }) {
                    Text(
                        "Log Out",
                        color = MainActivity.ColorStorage.textPurple
                    )
                }
            }

            // **Contact Us Dialog**
            if (showContactUsDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showContactUsDialog = false
                    },
                    title = {
                        Text(
                            text = "Contact Us",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "You can reach us at:",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "info@scholarly.com",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White

                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "For urgent assistance: ",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White

                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "587-600-3626",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White

                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Copy the email address to clipboard
                                clipboardManager.setText(
                                    AnnotatedString("info@scholarly.com")
                                )
                                Toast.makeText(
                                    context,
                                    "Email address copied to clipboard",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showContactUsDialog = false
                            }
                        ) {
                            Text("Copy", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showContactUsDialog = false
                            }
                        ) {
                            Text("Close", color = Color.White)
                        }
                    },
                    containerColor = darkGrayCustom
                )
            }
        }
    }
}
