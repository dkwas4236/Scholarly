package com.example.scholarly.screens

import androidx.compose.foundation.MutatePriority
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.scholarly.MainActivity
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.ui.theme.Purple40
import com.example.scholarly.ui.theme.darkGrayCustom
import com.example.scholarly.viewmodel.UserViewModel
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMeeting(navController: NavController) {
    val context = LocalContext.current
    val databaseHelper = DatabaseHelper(context)

    var selectedSubject = remember { mutableStateOf("Mathematics") }
    var selectedDate = remember { mutableStateOf("") }
    var selectedTimeSlot = remember { mutableStateOf("Morning") }
    var errorMessage = remember { mutableStateOf("") }
    var showDatePicker = remember { mutableStateOf(false) }
    var selectedDayOfWeek = remember { mutableStateOf("") }

    // available options
    val subjects = listOf("Mathematics", "English", "Science")
    val timeSlots = listOf("Morning", "Evening") // changed to morning and evening only

    // Get current date for date picker
    val calendar = Calendar.getInstance()
    val today = calendar.timeInMillis
    calendar.add(Calendar.MONTH, 1) // Allow booking up to 1 month in advance
    val oneMonthFromNow = calendar.timeInMillis

    // Date picker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = today,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in today..oneMonthFromNow
            }
        }
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MainActivity.ColorStorage.backgroundPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Book Meeting Session",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Subject Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Select Subject",
                        color = MainActivity.ColorStorage.textPurple,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    subjects.forEach { subject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedSubject.value == subject,
                                onClick = { selectedSubject.value = subject }
                            )
                            Text(
                                text = subject,
                                color = MainActivity.ColorStorage.textPurple,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Date Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Select Date",
                        color = MainActivity.ColorStorage.textPurple,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FilledTonalButton(
                        onClick = { showDatePicker.value = true },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MainActivity.ColorStorage.backgroundPurple,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            if (selectedDate.value.isEmpty()) "Choose Date"
                            else selectedDate.value
                        )
                    }
                }
            }

            // Date Picker Dialog
            if (showDatePicker.value) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { milliseconds ->
                                selectedDate.value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                    .format(Date(milliseconds))

                                val selectedCalendar = Calendar.getInstance().apply {
                                    timeInMillis = milliseconds

                                }
                                // get the day of the week for the selected date
                                val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                                dayOfWeekFormat.timeZone = TimeZone.getTimeZone("Canada/Edmonton")
                                selectedDayOfWeek.value = dayOfWeekFormat.format(selectedCalendar.time)
                            }
                            showDatePicker.value = false
                        }) {
                            Text("Confirm", color = MainActivity.ColorStorage.textPurple)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDatePicker.value = false
                        }) {
                            Text("Cancel", color = MainActivity.ColorStorage.textPurple)
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Time Slot Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Select Time",
                        color = MainActivity.ColorStorage.textPurple,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    timeSlots.forEach { timeSlot ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTimeSlot.value == timeSlot,
                                onClick = { selectedTimeSlot.value = timeSlot }
                            )
                            Text(
                                text = timeSlot,
                                color = MainActivity.ColorStorage.textPurple,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Book Session Button
            ElevatedButton(
                onClick = {
                    when {
                        selectedDate.value.isEmpty() -> {
                            errorMessage.value = "Please select a date"
                        }
                        else -> {
                            // navigate to BookingConfirmationScreen with parameters
                            navController.navigate("bookingConfirmation/${selectedDate.value}/${selectedDayOfWeek.value}/${selectedTimeSlot.value}/${selectedSubject.value}")
                        }
                    }
                }
            ) {
                Text("Book Session", color = MainActivity.ColorStorage.textPurple)
            }


            Spacer(modifier = Modifier.height(10.dp))

            // Cancel Button
            FilledTonalButton(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt,
                    contentColor = MainActivity.ColorStorage.textPurple
                )
            ) {
                Text("Cancel")
            }

            // Error message
            if (errorMessage.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage.value,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTutoringSessionScreen() {
    val navController = rememberNavController()
    BookMeeting(navController = navController)
}