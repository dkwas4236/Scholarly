package com.example.scholarly.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.models.Meeting
import com.example.scholarly.ui.theme.darkGrayCustom
import com.example.scholarly.viewmodel.MeetingsViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailsScreen(meetingID: Int, meetingsViewModel: MeetingsViewModel, navController: NavController) {
    val meeting = meetingsViewModel.getMeetingById(meetingID) ?: Meeting(
        meetingID = 1,
        studentID = 10001,
        tutorID = 2001,
        date = "December 12, 2024",
        startTime = "14:00",
        endTime = "15:00"
    )
    var showMessage by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkGrayCustom
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween  // Adjusts the arrangement to space between
        ) {
            Column {
                TopAppBar(
                    title = {
                        Text("Meeting Details", color = Color.White, fontSize = 24.sp)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Go back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MainActivity.ColorStorage.backgroundPurple)
                )

                Spacer(modifier = Modifier.height(24.dp))  // Increased space after the app bar

                Text(text = "Meeting ID: ${meeting.meetingID}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))  // Space between text elements
                Text(text = "Student ID: ${meeting.studentID}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Student Email: ${meetingsViewModel.getStudentEmailFromID(meeting.studentID)}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Tutor ID: ${meeting.tutorID}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Date: ${meeting.date}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Time Slot: ${meeting.startTime}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            }

            Button(
                onClick = {
                    meetingsViewModel.cancelMeeting(meeting.meetingID)
                    showMessage = true
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors( Color.White, contentColor = Color.Black)
            ) {
                Text("Cancel Meeting", fontSize = 18.sp)
            }
            if (showMessage) {
                Text(
                    text = "Meeting Cancelled",
                    color = Color.Green,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000) // Delay for 2 seconds
                    navController.navigateUp()
                    showMessage = false
                }
            }
        }
    }
}
