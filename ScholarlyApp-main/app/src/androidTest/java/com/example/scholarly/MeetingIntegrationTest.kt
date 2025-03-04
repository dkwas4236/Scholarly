package com.example.scholarly
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.scholarly.database.DatabaseHelper
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MeetingIntegrationTest {
    private lateinit var databaseHelper: DatabaseHelper

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        databaseHelper = DatabaseHelper(context)
    }

    @After
    fun teardown() {
        databaseHelper.close()
    }

    @Test
    fun testStudentBooksMeetingSuccessfully() = runBlocking {
        // Insert tutor and student
        val tutorId = databaseHelper.insertTutor(
            "Jane",
            "Doe",
            "9876543210",
            "jane.doe@example.com",
            "password",
            "true"
        ).toInt()
        val studentId = databaseHelper.insertStudent(
            "John",
            "Smith",
            "1234567890",
            "john.smith@example.com",
            "password123"
        ).toInt()

        // Add availability for morning slot
        databaseHelper.insertAvailability(
            tutorId,
            "Monday",
            isMorningAvailable = true,
            isEveningAvailable = false
        )

        // Attempt to book a meeting at the available morning slot
        val bookingResult =
            databaseHelper.bookMeetingIfAvailable(tutorId, "2024-12-02", "morning", studentId)

        // Assert that the meeting was booked successfully
        assertEquals("Meeting booked successfully!", bookingResult)
    }


    @Test
    fun testStudentFailsToBookMeetingWhenSlotUnavailable() = runBlocking {
        // Insert tutor and student
        val tutorId = databaseHelper.insertTutor(
            "Jane",
            "Doe",
            "9876543210",
            "jane.doe@example.com",
            "password",
            "true"
        ).toInt()
        val studentId = databaseHelper.insertStudent(
            "John",
            "Smith",
            "1234567890",
            "john.smith@example.com",
            "password123"
        ).toInt()

        // Add availability for morning slot
        databaseHelper.insertAvailability(
            tutorId,
            "Monday",
            isMorningAvailable = true,
            isEveningAvailable = false
        )

        // Insert an existing meeting for the morning slot
        databaseHelper.insertMeeting(studentId, tutorId, "2024-12-02", "morning", "morning")

        // Attempt to book a meeting at the morning slot, which is now unavailable
        val bookingResult =
            databaseHelper.bookMeetingIfAvailable(tutorId, "2024-12-02", "morning", studentId)

        // Assert that the booking fails due to the slot being unavailable
        assertEquals("Sorry, the tutor is already booked at this time.", bookingResult)
    }
}
