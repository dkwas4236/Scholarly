package com.example.scholarly.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.ui.theme.Purple40
import com.example.scholarly.ui.theme.darkGrayCustom
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.format.DateTimeFormatter
import android.content.Intent
import android.net.Uri
import com.example.scholarly.viewmodel.MeetingsViewModel
import com.example.scholarly.viewmodel.UserViewModel
import android.util.Log

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.sp
import com.example.scholarly.MainActivity
import com.example.scholarly.models.Meeting


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentScreen(
    navController: NavController,
    bottomBarState: MutableState<Boolean>,
    meetingsViewModel: MeetingsViewModel,
    userViewModel: UserViewModel,
) {
    val studentId = userViewModel.getCurrentUserId() // Fetch upcoming meetings for the student
    val meetingsAfterToday: List<Meeting> = studentId?.let {
        meetingsViewModel.getFutureMeetingsByStudentID(it).filterNotNull()
    } ?: emptyList()

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BottomStudentBar(navController, bottomBarState)
            }
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = darkGrayCustom
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Content Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    // Student greeting at the top
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Welcome, Student!",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Calendar setup
                    val calendarState = rememberSelectableCalendarState(
                        initialSelectionMode = SelectionMode.Single
                    )

                    SelectableCalendar(
                        calendarState = calendarState,
                        monthHeader = { monthState ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    monthState.currentMonth = monthState.currentMonth.minusMonths(1)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Previous month",
                                        tint = Color.White
                                    )
                                }
                                Text(
                                    text = monthState.currentMonth.format(
                                        DateTimeFormatter.ofPattern("MMMM yyyy")
                                    ),
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                IconButton(onClick = {
                                    monthState.currentMonth = monthState.currentMonth.plusMonths(1)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Next month",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        dayContent = { dayState: DayState<*> ->
                            StudentDayContent(
                                dayState = dayState,
                                isSelected = calendarState.selectionState.selection.contains(dayState.date),
                                onDayClick = {
                                    calendarState.selectionState.onDateSelected(dayState.date)
                                }
                            )
                        }
                    )

                    val selectedDate = calendarState.selectionState.selection.firstOrNull()
                    Text(
                        text = selectedDate?.let { "Selected date: $it" } ?: "No date selected",
                        color = Color.White
                    )

                    // Upcoming Meetings Section
                    if (meetingsAfterToday.isNotEmpty()) {
                        Log.d("StudentScreen", "Upcoming Meeting Dates: $meetingsAfterToday")
                        MeetingListSection(
                            title = "Upcoming Meetings",
                            meetings = meetingsAfterToday,
                            navController = navController
                        )
                    } else {
                        Text(
                            text = "No upcoming meetings.",
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Add space between the meetings section and the bottom content
                    Spacer(modifier = Modifier.height(40.dp))
                }

                // Row for the buttons on the left and right
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Ensures space between left and right buttons
                ) {
                    // Left Button
                    ElevatedButton(
                        onClick = { navController.navigate("googleMeetCode") },
                        modifier = Modifier
                            .widthIn(min = 150.dp) // Optional: adjust the minimum width
                            .padding(start = 8.dp) // Optional: add space to the right
                    ) {
                        Text("Join Google Meet", color = MainActivity.ColorStorage.textPurple)
                    }

                    // Right Button
                    ElevatedButton(
                        onClick = { navController.navigate("bookMeeting") },
                        modifier = Modifier
                            .widthIn(min = 150.dp)
                            .padding(end = 8.dp)
                    ) {
                        Text("Book a Meeting", color = MainActivity.ColorStorage.textPurple)
                    }
                }
            }
        }
    }
}


// Custom DayContent composable to customize the appearance of each day
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentDayContent(
    dayState: DayState<*>,
    isSelected: Boolean,
    onDayClick: () -> Unit
) {
    val date = dayState.date

    val backgroundColor = if (isSelected) Purple40 else Color.White // Purple color for selected date
    val textColor = Color.Black

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .clickable { onDayClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor
        )
    }
}
