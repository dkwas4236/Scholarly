package com.example.scholarly.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.models.Meeting
import com.example.scholarly.viewmodel.MeetingsViewModel
import androidx.compose.foundation.background
import androidx.compose.runtime.MutableState
import com.example.scholarly.MainActivity

@Composable
fun MeetingsScreen(meetingsViewModel: MeetingsViewModel, navController: NavController, bottomBarState: MutableState<Boolean>) {

    // This line will update the UI automatically whenever the meetings list changes.
    val meetings = meetingsViewModel.getAllMeetings()

    // Using Scaffold to provide a bottom bar layout
    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BottomBar(navController, bottomBarState)
            }
        } // Call to the BottomBar function
    ) { paddingValues ->
        // LazyColumn for displaying a scrollable list of meetings
        LazyColumn(
            modifier = Modifier
                .background(MainActivity.ColorStorage.listBackgroundGrey)
                .fillMaxSize()  // Makes sure to fill the screen
                .padding(paddingValues) // Apply padding from the Scaffold
        ) {
            // For each meeting in the meetings list, it creates a MeetingItem
            items(meetings) { meeting ->
                MeetingItem(meeting) {
                    navController.navigate("meetingDetails/${meeting.meetingID}")
                }
            }
        }
    }
}

@Composable
fun MeetingItem(meeting: Meeting, onClick: () -> Unit) {
    // Card is used to visually separate the meeting items with padding and make it clickable
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        // Column layout to arrange the meeting details vertically inside the Card
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Meeting ID: ${meeting.meetingID}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )  // Set text color to white
            Text(
                text = "Date: ${meeting.date}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )  // Set text color to white
        }
    }
}
