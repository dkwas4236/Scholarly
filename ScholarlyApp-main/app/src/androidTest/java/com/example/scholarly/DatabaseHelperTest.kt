package com.example.scholarly

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.scholarly.database.DatabaseHelper
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_STUDENT_EMAIL
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_STUDENT_FIRST_NAME
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_STUDENT_ID
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_STUDENT_LAST_NAME
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_STUDENT_PASSWORD
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_STUDENT_PHONE_NUMBER
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_EMAIL
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_FIRST_NAME
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_ID
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_LAST_NAME
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_PASSWORD
import com.example.scholarly.database.DatabaseHelper.Companion.COLUMN_TUTOR_PHONE_NUMBER
import com.example.scholarly.database.DatabaseHelper.Companion.TABLE_STUDENT
import com.example.scholarly.database.DatabaseHelper.Companion.TABLE_TUTOR

@RunWith(AndroidJUnit4::class)
class DatabaseHelperTest {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db: SQLiteDatabase

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    @After
    fun tearDown() {
        // Clean up after tests
        dbHelper.close()
    }

    @Test
    fun testTableCreation() {
        // Check if the tables are created
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='${TABLE_STUDENT}';", null)
        assertTrue("Student table is not created", cursor.count > 0)
        cursor.close()

        val cursorTutor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='${TABLE_TUTOR}';", null)
        assertTrue("Tutor table is not created", cursorTutor.count > 0)
        cursorTutor.close()
    }

    @Test
    fun testInsertAndRetrieveStudent() {
        // Insert a student
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_FIRST_NAME, "John")
            put(COLUMN_STUDENT_LAST_NAME, "Doe")
            put(COLUMN_STUDENT_PHONE_NUMBER, "1234567890")
            put(COLUMN_STUDENT_EMAIL, "john.doe@example.com")
            put(COLUMN_STUDENT_PASSWORD, "password123")
        }
        val studentId = db.insert(TABLE_STUDENT, null, values)
        assertTrue("Failed to insert student", studentId != -1L)

        // Retrieve the student
        val cursor = db.query(
            TABLE_STUDENT, null, "$COLUMN_STUDENT_ID=?", arrayOf(studentId.toString()),
            null, null, null
        )
        assertTrue("Failed to retrieve inserted student", cursor.moveToFirst())
        assertEquals("John", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_FIRST_NAME)))
        cursor.close()
    }

    @Test
    fun testUpdateStudent() {
        // Insert a student
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_FIRST_NAME, "John")
            put(COLUMN_STUDENT_LAST_NAME, "Doe")
            put(COLUMN_STUDENT_PHONE_NUMBER, "1234567890")
            put(COLUMN_STUDENT_EMAIL, "john.doe@example.com")
            put(COLUMN_STUDENT_PASSWORD, "password123")
        }
        val studentId = db.insert(TABLE_STUDENT, null, values)
        assertTrue("Failed to insert student", studentId != -1L)

        // Update the student
        val updateValues = ContentValues().apply {
            put(COLUMN_STUDENT_EMAIL, "john.updated@example.com")
        }
        val rowsUpdated = db.update(TABLE_STUDENT, updateValues, "$COLUMN_STUDENT_ID=?", arrayOf(studentId.toString()))
        assertEquals(1, rowsUpdated)

        // Verify the update
        val cursor = db.query(
            TABLE_STUDENT, null, "$COLUMN_STUDENT_ID=?", arrayOf(studentId.toString()),
            null, null, null
        )
        assertTrue(cursor.moveToFirst())
        assertEquals("john.updated@example.com", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_EMAIL)))
        cursor.close()
    }

    @Test
    fun testDeleteStudent() {
        // Insert a student
        val values = ContentValues().apply {
            put(COLUMN_STUDENT_FIRST_NAME, "John")
            put(COLUMN_STUDENT_LAST_NAME, "Doe")
            put(COLUMN_STUDENT_PHONE_NUMBER, "1234567890")
            put(COLUMN_STUDENT_EMAIL, "john.doee@example.com")
            put(COLUMN_STUDENT_PASSWORD, "password123")
        }
        val studentId = db.insert(TABLE_STUDENT, null, values)
        assertTrue("Failed to insert student, ID: $studentId", studentId != -1L)

        // Verify insertion
        val cursorInsert = db.query(
            TABLE_STUDENT, null, "$COLUMN_STUDENT_ID=?", arrayOf(studentId.toString()),
            null, null, null
        )
        assertTrue("Inserted student not found", cursorInsert.moveToFirst())
        cursorInsert.close()

        // Delete the student
        val rowsDeleted = db.delete(TABLE_STUDENT, "$COLUMN_STUDENT_ID=?", arrayOf(studentId.toString()))
        assertEquals("Student delete operation failed", 1, rowsDeleted)

        // Verify deletion
        val cursorDelete = db.query(
            TABLE_STUDENT, null, "$COLUMN_STUDENT_ID=?", arrayOf(studentId.toString()),
            null, null, null
        )
        assertFalse("Student was not deleted", cursorDelete.moveToFirst())
        cursorDelete.close()
    }

    @Test
    fun testInsertAndRetrieveTutor() {
        val values = ContentValues().apply {
            put(COLUMN_TUTOR_FIRST_NAME, "Jane")
            put(COLUMN_TUTOR_LAST_NAME, "Smith")
            put(COLUMN_TUTOR_PHONE_NUMBER, "0987654321")
            put(COLUMN_TUTOR_EMAIL, "jane.smith@example.com")
            put(COLUMN_TUTOR_PASSWORD, "password456")
        }
        val tutorId = db.insert(TABLE_TUTOR, null, values)
        assertTrue("Failed to insert tutor", tutorId != -1L)

        // Retrieve the tutor
        val cursor = db.query(
            TABLE_TUTOR, null, "$COLUMN_TUTOR_ID=?", arrayOf(tutorId.toString()),
            null, null, null
        )
        assertTrue("Failed to retrieve inserted tutor", cursor.moveToFirst())
        assertEquals("Jane", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_FIRST_NAME)))
        cursor.close()
    }
    @Test
    fun testUpdateTutor() {
        val values = ContentValues().apply {
            put(COLUMN_TUTOR_FIRST_NAME, "Jane")
            put(COLUMN_TUTOR_LAST_NAME, "Smith")
            put(COLUMN_TUTOR_PHONE_NUMBER, "0987654321")
            put(COLUMN_TUTOR_EMAIL, "jane.smithh@example.com")
            put(COLUMN_TUTOR_PASSWORD, "password456")
        }
        val tutorId = db.insert(TABLE_TUTOR, null, values)
        assertTrue("Failed to insert tutor", tutorId != -1L)

        // Update the tutor's email
        val updateValues = ContentValues().apply {
            put(COLUMN_TUTOR_EMAIL, "jane.updated@example.com")
        }
        val rowsUpdated = db.update(TABLE_TUTOR, updateValues, "$COLUMN_TUTOR_ID=?", arrayOf(tutorId.toString()))
        assertEquals(1, rowsUpdated)

        // Verify the update
        val cursor = db.query(
            TABLE_TUTOR, null, "$COLUMN_TUTOR_ID=?", arrayOf(tutorId.toString()),
            null, null, null
        )
        assertTrue(cursor.moveToFirst())
        assertEquals("jane.updated@example.com", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_EMAIL)))
        cursor.close()
    }


    @Test
    fun testDeleteTutor() {
        val values = ContentValues().apply {
            put(COLUMN_TUTOR_FIRST_NAME, "Jane")
            put(COLUMN_TUTOR_LAST_NAME, "Smith")
            put(COLUMN_TUTOR_PHONE_NUMBER, "0987654321")
            put(COLUMN_TUTOR_EMAIL, "jane.smithh@example.com")
            put(COLUMN_TUTOR_PASSWORD, "password456")
        }
        val tutorId = db.insert(TABLE_TUTOR, null, values)
        assertTrue("Failed to insert tutor", tutorId != -1L)


        val cursorInsert = db.query(
            TABLE_TUTOR, null, "$COLUMN_TUTOR_ID=?", arrayOf(tutorId.toString()),
            null, null, null
        )
        assertTrue("Inserted tutor not found", cursorInsert.moveToFirst())
        cursorInsert.close()

        // Delete the tutor
        val rowsDeleted = db.delete(TABLE_TUTOR, "$COLUMN_TUTOR_ID=?", arrayOf(tutorId.toString()))
        assertEquals("Tutor delete operation failed", 1, rowsDeleted)

        // Verify deletion
        val cursorDelete = db.query(
            TABLE_TUTOR, null, "$COLUMN_TUTOR_ID=?", arrayOf(tutorId.toString()),
            null, null, null
        )
        assertFalse("Tutor was not deleted", cursorDelete.moveToFirst())
        cursorDelete.close()
    }

    // Additional helper method to check if the tutor exists
    @Test
    fun testTutorExists() {
        val values = ContentValues().apply {
            put(COLUMN_TUTOR_FIRST_NAME, "Alice")
            put(COLUMN_TUTOR_LAST_NAME, "Brown")
            put(COLUMN_TUTOR_PHONE_NUMBER, "1122334455")
            put(COLUMN_TUTOR_EMAIL, "alice.brown@example.com")
            put(COLUMN_TUTOR_PASSWORD, "password789")
        }
        val tutorId = db.insert(TABLE_TUTOR, null, values)
        assertTrue("Failed to insert tutor", tutorId != -1L)

        // Verify that the tutor exists in the table
        val cursor = db.query(
            TABLE_TUTOR, null, "$COLUMN_TUTOR_ID=?", arrayOf(tutorId.toString()),
            null, null, null
        )
        assertTrue("Tutor does not exist in the database", cursor.moveToFirst())
        cursor.close()
    }
}