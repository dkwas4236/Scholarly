package com.example.scholarly

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_APPROVED
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_ID
import com.example.scholarly.database.DatabaseHelper.Companion.TABLE_TUTOR
import com.example.scholarly.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApprovalIntegrationTest {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setup() {
        // Initialize your database
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        databaseHelper = DatabaseHelper(context)
        userViewModel = UserViewModel(databaseHelper)
    }

    private fun insertTestTutor(): Int {
        val tutorId = databaseHelper.insertTutor("John", "Doe", "1234567890", "john.doe@example.com", "password", "false")
        return tutorId.toInt()
    }

    @Test
    fun testApproveTutor() = runBlocking {
        // Insert tutor
        val tutorId = databaseHelper.insertTutor("John", "Doe", "1234567890", "john@example.com", "password", "false")

        userViewModel.approveTutor(tutorId.toInt())

        delay(200)

        // Check if the tutor is approved
        val tutors = databaseHelper.getAllTutors()
        val isApproved = tutors.any { it.tutorID.toLong() == tutorId && it.approved == "true" }

        assertTrue("Tutor should be approved", isApproved)
    }


    @Test
    fun testDenyTutor() {
        val tutorId = insertTestTutor()

        // Deny the tutor
        userViewModel.denyTutor(tutorId)

        // Verify denial status
        val cursor = databaseHelper.writableDatabase.query(
            TABLE_TUTOR, null,
            "$COLUMN_TUTOR_ID = ?", arrayOf(tutorId.toString()), null, null, null
        )
        if (cursor.moveToFirst()) {
            val isApproved = cursor.getString(cursor.getColumnIndex(COLUMN_TUTOR_APPROVED))
            assertTrue("Tutor should not be approved", isApproved == "false")
        } else {
            fail("No tutor found with the given ID")
        }
        cursor.close()
    }

    @After
    fun tearDown() {
        // Clear data or close the database connection
        databaseHelper.writableDatabase.execSQL("DELETE FROM $TABLE_TUTOR")
        databaseHelper.close()
    }
}