package com.example.scholarly

import androidx.test.platform.app.InstrumentationRegistry
import com.example.scholarly.database.DatabaseHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
@RunWith(JUnit4::class)

class AvailabilityIntegrationTest {
    private lateinit var dbHelper: DatabaseHelper
    @Before
    fun setup() {
        dbHelper = DatabaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        insertMockAvailabilityData()
    }
    @After
    fun tearDown() {
        //dbHelper.clearAllData()
        dbHelper.close()
    }
    private fun insertMockAvailabilityData() {
        // Mock data for tutor with ID 1
        dbHelper.insertAvailability(tutorID = 1, day = "Monday", isMorningAvailable = true, isEveningAvailable = false)
        dbHelper.insertAvailability(tutorID = 1, day = "Wednesday", isMorningAvailable = true, isEveningAvailable = true)
    }
    @Test
    fun testGetAvailabilityByTutor() {
        // tutor ID 1 with known availability data in the test DB
        val tutorId = 1
        val availabilityList = dbHelper.getAvailabilityByTutor(tutorId)
        // Assert that the list is not empty
        assertNotNull("Availability list should not be null", availabilityList)
        assertTrue("Tutor should have availability data", availabilityList.isNotEmpty())
        assertEquals("Expected day for availability", "Monday", availabilityList[0].day)
        assertTrue("Expected morning availability", availabilityList[0].isMorningAvailable)
        assertFalse("Expected no evening availability", availabilityList[0].isEveningAvailable)
    }
}