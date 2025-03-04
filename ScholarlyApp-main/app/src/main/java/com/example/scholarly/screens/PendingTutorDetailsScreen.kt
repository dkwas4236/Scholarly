package com.example.scholarly.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.ui.theme.darkGrayCustom
import com.example.scholarly.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingTutorDetailsScreen(
    navController: NavController,
    tutorId: Int,
    userViewModel: UserViewModel
) {
    var tutor by remember { mutableStateOf(userViewModel.tutors.find { it.tutorID == tutorId }) }

    LaunchedEffect(tutorId) {
        if (tutor == null) {
            userViewModel.loadTutors()
            tutor = userViewModel.tutors.find { it.tutorID == tutorId }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkGrayCustom
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = { Text("Tutor Details", color = Color.White, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurple
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            tutor?.let { currentTutor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "First Name: ${currentTutor.firstName}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Last Name: ${currentTutor.lastName}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Email: ${currentTutor.email}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Phone: ${currentTutor.phoneNumber}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            userViewModel.approveTutor(currentTutor.tutorID)
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainActivity.ColorStorage.approveGreen
                        )
                    ) {
                        Text("Approve")
                    }

                    Button(
                        onClick = {
                            userViewModel.denyTutor(currentTutor.tutorID)
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainActivity.ColorStorage.denyRed
                        )
                    ) {
                        Text("Deny")
                    }
                }
            } ?: Text(
                text = "Tutor not found",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}