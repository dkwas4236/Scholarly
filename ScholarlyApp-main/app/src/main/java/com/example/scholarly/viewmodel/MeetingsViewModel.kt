package com.example.scholarly.viewmodel

import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.models.Meeting
import com.example.scholarly.models.TutorWithQualification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MeetingsViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    private val _meetings = MutableStateFlow<List<Meeting>>(emptyList())
    val meetings: StateFlow<List<Meeting>> = _meetings
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // MutableState for managing UI feedback
    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _meetingBooked = mutableStateOf(false)
    val meetingBooked: State<Boolean> = _meetingBooked

    init {
        loadMeetings()
    }

    private fun loadMeetings() {
        // Load meetings from the database and update the _meetings state
        _meetings.value = databaseHelper.getAllMeetings()
    }

    fun getAllMeetings(): List<Meeting> {
        return _meetings.value
   }

    fun getMeetingById(meetingID: Int): Meeting? {
        return _meetings.value.find { it.meetingID == meetingID }
    }

    fun getMeetingByTutorID(tutorID: Int): Meeting? {
        return _meetings.value.find { it.tutorID == tutorID }
    }

    fun getMeetingByStudentID(studentID: Int): Meeting? {
        return _meetings.value.find { it.studentID == studentID }
    }

    fun getFutureMeetingsByTutorID(tutorID: Int): List<Meeting> {
        val today = dateFormatter.format(Date())

        return _meetings.value.filter {
            it.tutorID == tutorID && it.date >= today
        }
    }

    fun getPastMeetingsByTutorID(tutorID: Int): List<Meeting> {
        val today = dateFormatter.format(Date())

        return _meetings.value.filter {
            it.tutorID == tutorID && it.date < today
        }
    }

    fun getFutureMeetingsByStudentID(studentID: Int): List<Meeting> {
        val today = dateFormatter.format(Date())

        return _meetings.value.filter {
            it.studentID == studentID && it.date >= today
        }
    }

    fun getPastMeetingsByStudentID(studentID: Int): List<Meeting> {
        val today = dateFormatter.format(Date())

        return _meetings.value.filter {
            it.studentID == studentID && it.date < today
        }
    }


    fun getMeetingsForDate(tutorID: Int, date: LocalDate): List<Meeting> {
        val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        return _meetings.value.filter {
            it.tutorID == tutorID && it.date == dateStr
        }
    }

    fun cancelMeeting(meetingId: Int) {
        viewModelScope.launch {
            databaseHelper.deleteMeetingById(meetingId)
            _meetings.value = _meetings.value.filter { it.meetingID != meetingId }
        }
    }

    fun isSlotAvailable(tutorId: Int, date: String, timeSlot: String): Boolean {
        return databaseHelper.isSlotAvailable(tutorId, date, timeSlot)
    }

    fun bookMeetingIfAvailable(tutorId: Int, date: String, timeSlot: String, studentId: Int): Boolean {
        return databaseHelper.bookMeeting(tutorId, date, timeSlot, studentId)
    }

    fun findAvailableTutors(selectedDayOfWeek: String, selectedTimeSlot: String, selectedSubject: String): List<TutorWithQualification> {
        return databaseHelper.findTutorsByAvailability(selectedDayOfWeek, selectedTimeSlot, selectedSubject)
    }

    fun getStudentEmailFromID(studentID: Int): String? {
        return databaseHelper.getStudentEmailFromID(studentID)
    }
}



