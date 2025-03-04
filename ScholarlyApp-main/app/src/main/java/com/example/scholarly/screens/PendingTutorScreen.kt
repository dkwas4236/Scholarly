package com.example.scholarly.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.ui.theme.darkGrayCustom
import com.example.scholarly.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingTutorScreen(
    navController: NavController,
    bottomBarState: MutableState<Boolean>,
    userViewModel: UserViewModel
) {
    LaunchedEffect(Unit) {
        userViewModel.loadTutors()
    }

    val pendingTutors = userViewModel.tutors.filter { it.approved == "false" }

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
            color = darkGrayCustom
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                if (pendingTutors.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No pending tutors",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    pendingTutors.forEach { tutor ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("pendingTutorDetails/${tutor.tutorID}")
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${tutor.firstName} ${tutor.lastName}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                MainActivity.ColorStorage.approveGreen,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    ) {
                                        IconButton(onClick = {
                                            userViewModel.approveTutor(tutor.tutorID)
                                        }) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = "Approve",
                                                tint = Color.White
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                MainActivity.ColorStorage.denyRed,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    ) {
                                        IconButton(onClick = {
                                            userViewModel.denyTutor(tutor.tutorID)
                                        }) {
                                            Icon(
                                                Icons.Filled.Close,
                                                contentDescription = "Deny",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}