package com.example.scholarly.screens

import android.content.Intent
import android.net.Uri
import android.util.Log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.models.Meeting
import com.example.scholarly.ui.theme.Purple40
import com.example.scholarly.ui.theme.darkGrayCustom
import com.example.scholarly.viewmodel.MeetingsViewModel
import com.example.scholarly.viewmodel.UserViewModel
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalContext
import com.example.scholarly.MainActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorScreen(
    navController: NavController,
    meetingsViewModel: MeetingsViewModel,
    userViewModel: UserViewModel,
    bottomBarState: MutableState<Boolean>
) {
    // State variables for meeting code input
    var meetingCode by remember { mutableStateOf("") }

    // State for showing a success or error message
    var emailSent by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    // Context for launching the email intent
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BottomTutorBar(navController, bottomBarState)
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Welcome, Tutor",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Calendar
                val calendarState = rememberSelectableCalendarState(
                    initialSelectionMode = SelectionMode.Single
                )

                // Initialize and store the selected date
                var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
                LaunchedEffect(calendarState.selectionState.selection) {
                    selectedDate = calendarState.selectionState.selection.firstOrNull()
                }

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
                            IconButton(
                                onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Previous month",
                                    tint = Color.White // Set arrow color to white
                                )
                            }
                            val monthYearText = monthState.currentMonth.format(
                                DateTimeFormatter.ofPattern("MMMM yyyy")
                            )
                            Text(
                                text = monthYearText,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(
                                onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Next month",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    dayContent = { dayState: DayState<*> ->
                        DayContent(
                            dayState = dayState,
                            isSelected = calendarState.selectionState.selection.contains(dayState.date),
                            onDayClick = {
                                calendarState.selectionState.onDateSelected(dayState.date)
                            }
                        )
                    }
                )

                // Display the selected date
                selectedDate?.let {
                    Text(
                        text = "Selected date: $it",
                        color = Color.White
                    )
                } ?: Text(
                    text = "No date selected",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // "Set Availability" button
                ElevatedButton(
                    onClick = {
                        navController.navigate("availability")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Set Availability",
                        color = MainActivity.ColorStorage.textPurple
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "Start New Google Meet" button
                val context = LocalContext.current

                ElevatedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://meet.google.com/new")
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Start New Google Meet",
                        color = MainActivity.ColorStorage.textPurple
                    )
                }


                Spacer(modifier = Modifier.height(32.dp))

                // **New Section: Send Meeting Code via Email**
                Text(
                    text = "Send Meeting Code",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Meeting Code Input Field
                OutlinedTextField(
                    value = meetingCode,
                    onValueChange = { meetingCode = it },
                    label = { Text("Meeting Code", color = Color.White) },
                    placeholder = { Text("Enter meeting code", color = Color.Gray) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White), // Set input text color to white
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedTextColor = Color.White

                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Send Email Button
                Button(
                    onClick = {
                        if (meetingCode.isNotBlank()) {
                            // Create email intent
                            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "message/rfc822" // Specify email MIME type
                                putExtra(Intent.EXTRA_SUBJECT, "Scholarly: Your Meeting Code")
                                putExtra(Intent.EXTRA_TEXT, "Here is your meeting code: $meetingCode")
                            }

                            // Launch email chooser
                            val chooser = Intent.createChooser(emailIntent, "Choose an Email client:")

                            // Verify that there's an email app to handle the intent
                            if (emailIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(chooser)
                                emailSent = true
                                emailError = false
                            } else {
                                emailError = true
                            }
                        } else {
                            emailError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple40),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Send Meeting Code via Email",
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display success or error messages
                if (emailSent) {
                    Text(
                        text = "Email app opened successfully!",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (emailError) {
                    Text(
                        text = "Failed to open email app. Please try again.",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                // **End of New Section**

                // Existing Meetings List
                val tutorId = userViewModel.getCurrentUserId()
                // Combine future and past meetings
                val allMeetings: List<Meeting> = tutorId?.let { id ->
                    val futureMeetings = meetingsViewModel.getFutureMeetingsByTutorID(id)
                    val pastMeetings = meetingsViewModel.getPastMeetingsByTutorID(id)
                    (futureMeetings + pastMeetings).filterNotNull()
                } ?: emptyList()

                selectedDate?.let { date ->
                    val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    val selectedMeetings = tutorId?.let { id ->
                        meetingsViewModel.getMeetingsForDate(id, date)
                    } ?: emptyList()

                    if (selectedMeetings.isNotEmpty()) {
                        MeetingListSection(
                            title = "Meetings on ${date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                            meetings = selectedMeetings,
                            navController = navController
                        )
                    } else {
                        Text(
                            text = "No meetings scheduled on this date.",
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // List of upcoming meetings
                val meetingsAfterToday: List<Meeting> = tutorId?.let {
                    meetingsViewModel.getFutureMeetingsByTutorID(it).filterNotNull()
                } ?: emptyList()

                if (meetingsAfterToday.isNotEmpty()) {
                    Log.d("TutorScreen", "Upcoming Meeting Date: ${allMeetings}")
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
            }
        }
    }
}

@Composable
fun DayContent(
    dayState: DayState<*>,
    isSelected: Boolean,
    onDayClick: () -> Unit
) {
    val date = dayState.date

    val backgroundColor = if (isSelected) Purple40 else Color.White // Purple color for selected date
    val textColor = if (isSelected) Color.White else Color.Black

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

@Composable
fun MeetingListSection(
    title: String,
    meetings: List<Meeting>,
    navController: NavController
) {
    Text(
        text = title,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
    Column(
        modifier = Modifier
            //.verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        meetings.forEach { meeting ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .clickable {
                        // Navigate to MeetingDetailsScreen with the meeting ID
                        navController.navigate("meetingDetails/${meeting.meetingID}")
                    },
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Student ID: ${meeting.studentID}, Tutor ID: ${meeting.tutorID}",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Date: ${meeting.date} | Time Slot: ${meeting.startTime}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
