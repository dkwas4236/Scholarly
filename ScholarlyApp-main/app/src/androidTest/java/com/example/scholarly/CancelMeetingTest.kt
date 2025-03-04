package com.example.scholarly

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import android.content.Context
import com.example.scholarly.database.DatabaseHelper

class CancelMeetingTest {

    private lateinit var databaseHelper: DatabaseHelper

    @Before
    fun setup() {

        val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<Context>()
        databaseHelper = DatabaseHelper(context)
    }

    @After
    fun tearDown() {

        databaseHelper.close()
    }

    @Test
    fun testCancelMeeting() {
        val meetingId = 1
        val studentId = 101
        val tutorId = 201
        val date = "2024-12-01"
        val startTime = "10:00"
        val endTime = "11:00"

        // Insert the meeting
        val result = databaseHelper.insertMeeting(studentId, tutorId, date, startTime, endTime)
        assertNotEquals("Failed to insert meeting", -1L, result)

        //Cancel the meeting
        databaseHelper.deleteMeetingById(meetingId)

       //Verify the meeting has been deleted
        val meetingsAfterDeletion = databaseHelper.getAllMeetings()
        assertTrue("Meeting was not deleted", meetingsAfterDeletion.isEmpty())
    }

    @Test
    fun testCancelMeetingNoMeetingFound() {

        val meetingId = 999
        val rowsDeleted = databaseHelper.deleteMeetingById(meetingId)

        // Assert that no rows were deleted
        assertEquals("Expected 0 rows deleted", 0, rowsDeleted)
    }

    @Test
    fun testCancelMeetingCheckDatabaseAfterDeletion() {
        // Insert meeting
        val studentId = 101
        val tutorId = 201
        val date = "2024-12-01"
        val startTime = "10:00"
        val endTime = "11:00"

        // Insert the meeting and get the generated meeting ID
        val result = databaseHelper.insertMeeting(studentId, tutorId, date, startTime, endTime)
        assertNotEquals("Failed to insert meeting", -1L, result)

        // Retrieve the inserted meeting ID
        val insertedMeetingId = result.toInt()

        // Cancel the meeting
        databaseHelper.deleteMeetingById(insertedMeetingId)

        // Verify that no meetings exist in the database
        val meetingsAfterCancellation = databaseHelper.getAllMeetings()
        assertTrue("Meetings are not empty after cancellation", meetingsAfterCancellation.isEmpty())
    }


    @Test
    fun testCancelMeetingNotDeleted() {
        // Cancel a meeting that doesn't exist
        val meetingId = 999
        val rowsDeleted = databaseHelper.deleteMeetingById(meetingId)

        // Assert that no rows were deleted
        assertEquals("Expected 0 rows deleted", 0, rowsDeleted)
    }
}
