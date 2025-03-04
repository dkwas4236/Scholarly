package com.example.scholarly.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.models.TutorWithQualification
import com.example.scholarly.models.Tutor
import com.example.scholarly.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import com.example.scholarly.viewmodel.MeetingsViewModel


@Composable
fun BookingConfirmationScreen(
    navController: NavController,
    selectedDate: String,
    selectedDayOfWeek: String,
    selectedTimeSlot: String,
    selectedSubject: String,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val databaseHelper = DatabaseHelper(context)

    val studentId = userViewModel.getCurrentUserId() ?: return
    val tutors = remember { mutableStateOf<List<TutorWithQualification>>(emptyList()) }
    val selectedTutor = remember { mutableStateOf<TutorWithQualification?>(null) }
    val errorMessage = remember { mutableStateOf("") }

    // get available tutors when the screen is loaded
    LaunchedEffect(selectedDayOfWeek, selectedTimeSlot, selectedSubject) {
        tutors.value = databaseHelper.findTutorsByAvailability(selectedDayOfWeek, selectedTimeSlot, selectedSubject)
        if (tutors.value.isEmpty()) {
            errorMessage.value = "No tutors available for the selected time slot and subject."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Booking Confirmation", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Subject: $selectedSubject", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Date: $selectedDayOfWeek, $selectedDate", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Time Slot: $selectedTimeSlot", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (tutors.value.isNotEmpty()) {
            Text(
                text = "Please select a tutor:",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            for (tutorWithQualification in tutors.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            color = if (selectedTutor.value == tutorWithQualification) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedTutor.value = tutorWithQualification
                        }
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Tutor: ${tutorWithQualification.tutor.firstName} ${tutorWithQualification.tutor.lastName}, Qualification: ${tutorWithQualification.qualification}",
                        fontSize = 17.sp,
                        color = if (selectedTutor.value == tutorWithQualification) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            Text(
                text = errorMessage.value,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                selectedTutor.value?.let { tutor ->
                    val resultMessage = databaseHelper.bookMeetingIfAvailable(
                        tutorId = tutor.tutor.tutorID,
                        date = selectedDate,
                        timeSlot = selectedTimeSlot,
                        studentId = studentId
                    )

                    if (resultMessage == "Meeting booked successfully!") {
                        // meeting booking success
                        Toast.makeText(navController.context, resultMessage, Toast.LENGTH_SHORT).show()
                        navController.navigate("student")
                    } else {
                        // meeting booking failure
                        errorMessage.value = resultMessage
                    }
                }
            },
            enabled = tutors.value.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (tutors.value.isNotEmpty()) Color.Blue else Color.Gray,
                contentColor = Color.White
            )
        ) {
            Text("Book Meeting")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigateUp() }) {
            Text("Go Back")
        }

        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
