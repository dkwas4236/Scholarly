package com.example.scholarly

import androidx.test.platform.app.InstrumentationRegistry
import com.example.scholarly.database.DatabaseHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ClearAvailabilityTest {
    private lateinit var dbHelper: DatabaseHelper

    @Before
    fun setup() {
        dbHelper = DatabaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        insertMockAvailabilityData()
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    private fun insertMockAvailabilityData() {

        dbHelper.insertAvailability(tutorID = 1, day = "Monday", isMorningAvailable = true, isEveningAvailable = true)
        dbHelper.insertAvailability(tutorID = 1, day = "Tuesday", isMorningAvailable = true, isEveningAvailable = false)
        dbHelper.insertAvailability(tutorID = 2, day = "Wednesday", isMorningAvailable = false, isEveningAvailable = true)
    }

    @Test
    fun testClearAvailabilityForTutor() {
        val tutorIdToRemove = 1

        // Confirm tutor availability exists before clearing
        val availabilityBefore = dbHelper.getAvailabilityByTutor(tutorIdToRemove)
        assertTrue("Availability should exist for tutor before clearing", availabilityBefore.isNotEmpty())


        val result = dbHelper.clearAvailabilityForTutor(tutorIdToRemove)


        assertTrue("Clear availability method should return true", result)

        // Confirm tutor availability is cleared
        val availabilityAfter = dbHelper.getAvailabilityByTutor(tutorIdToRemove)
        assertTrue("Availability should be empty after clearing for the tutor", availabilityAfter.isEmpty())

        // Confirm other tutor's availability is unaffected
        val otherTutorAvailability = dbHelper.getAvailabilityByTutor(2)
        assertFalse("Other tutor's availability should not be affected", otherTutorAvailability.isEmpty())
        assertEquals("Other tutor's availability should remain unchanged", "Wednesday", otherTutorAvailability[0].day)
    }
}
