package com.example.scholarly.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.scholarly.models.Teach
import com.example.scholarly.models.Tutor
import com.example.scholarly.models.Student
import com.example.scholarly.models.Meeting
import com.example.scholarly.models.Admin
import com.example.scholarly.models.Availability
import com.example.scholarly.models.Subject
import com.example.scholarly.models.TutorWithQualification
import org.mindrot.jbcrypt.BCrypt
import java.text.SimpleDateFormat
import java.util.Locale


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        const val DATABASE_NAME = "395Database.db"
        const val DATABASE_VERSION = 15 // change this to refresh tables
        // darion: local version 15

        // student table
        const val TABLE_STUDENT = "STUDENT"
        const val COLUMN_STUDENT_ID = "studentID"
        const val COLUMN_STUDENT_FIRST_NAME = "firstName"
        const val COLUMN_STUDENT_LAST_NAME = "lastName"
        const val COLUMN_STUDENT_PHONE_NUMBER = "phoneNumber"
        const val COLUMN_STUDENT_EMAIL = "email"
        const val COLUMN_STUDENT_PASSWORD = "password"

        // teacher table
        const val TABLE_TUTOR = "TUTOR"
        const val COLUMN_TUTOR_ID = "tutorID"
        const val COLUMN_TUTOR_FIRST_NAME = "firstName"
        const val COLUMN_TUTOR_LAST_NAME = "lastName"
        const val COLUMN_TUTOR_PHONE_NUMBER = "phoneNumber"
        const val COLUMN_TUTOR_EMAIL = "email"
        const val COLUMN_TUTOR_PASSWORD = "password"
        const val COLUMN_TUTOR_APPROVED = "approval"

        // admin table
        const val TABLE_ADMIN = "ADMIN"
        const val COLUMN_ADMIN_ID = "adminID"
        const val COLUMN_ADMIN_EMAIL = "email"
        const val COLUMN_ADMIN_PASSWORD = "password"

        // subject table
        const val TABLE_SUBJECT = "SUBJECT"
        const val COLUMN_SUBJECT_ID = "subjectID"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"

        // teach table
        const val TABLE_TEACH = "TEACH"
        const val COLUMN_TEACH_ID = "teachID"
        const val COLUMN_QUALIFICATION_LEVEL = "qualificationLevel"

        // meeting table
        const val TABLE_MEETING = "MEETING"
        const val COLUMN_MEETING_ID = "meetingID"
        const val COLUMN_DATE = "date"
        const val COLUMN_START_TIME = "startTime"
        const val COLUMN_END_TIME = "endTime"

        // availability table
        private const val TABLE_AVAILABILITY = "AVAILABILITY"
        private const val COLUMN_AVAILABILITY_ID = "availabilityID"
        private const val COLUMN_DAY = "day"
        private const val COLUMN_IS_MORNING_AVAILABLE = "isMorningAvailable"
        private const val COLUMN_IS_EVENING_AVAILABLE = "isEveningAvailable"

    }

    private val createStudentTable = ("CREATE TABLE $TABLE_STUDENT ("
            + "$COLUMN_STUDENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_STUDENT_FIRST_NAME TEXT, "
            + "$COLUMN_STUDENT_LAST_NAME TEXT, "
            + "$COLUMN_STUDENT_PHONE_NUMBER TEXT, "
            + "$COLUMN_STUDENT_EMAIL TEXT UNIQUE, "
            + "$COLUMN_STUDENT_PASSWORD TEXT)")

    private val createTutorTable = ("CREATE TABLE $TABLE_TUTOR ("
            + "$COLUMN_TUTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_TUTOR_FIRST_NAME TEXT, "
            + "$COLUMN_TUTOR_LAST_NAME TEXT, "
            + "$COLUMN_TUTOR_PHONE_NUMBER TEXT, "
            + "$COLUMN_TUTOR_EMAIL TEXT UNIQUE, "
            + "$COLUMN_TUTOR_PASSWORD TEXT,"
            + "$COLUMN_TUTOR_APPROVED TEXT)")

    private val createSubjectTable = ("CREATE TABLE $TABLE_SUBJECT("
            + "$COLUMN_SUBJECT_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "$COLUMN_NAME TEXT,"
            + "$COLUMN_DESCRIPTION TEXT)" )

    private val createTeachTable = ("CREATE TABLE $TABLE_TEACH ("
            + "$COLUMN_TEACH_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_TUTOR_ID INTEGER,"
            + "$COLUMN_SUBJECT_ID INTEGER,"
            + "$COLUMN_QUALIFICATION_LEVEL TEXT,"
            + "FOREIGN KEY($COLUMN_TUTOR_ID) REFERENCES $TABLE_TUTOR($COLUMN_TUTOR_ID) ON DELETE CASCADE,"
            + "FOREIGN KEY($COLUMN_SUBJECT_ID) REFERENCES $TABLE_SUBJECT($COLUMN_SUBJECT_ID) ON DELETE CASCADE)")

    private val createMeetingTable = ("CREATE TABLE $TABLE_MEETING ("
            + "$COLUMN_MEETING_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_STUDENT_ID INTEGER, "
            + "$COLUMN_TUTOR_ID INTEGER, "
            + "$COLUMN_DATE TEXT, "
            + "$COLUMN_START_TIME TEXT, "
            + "$COLUMN_END_TIME TEXT, "
            + "FOREIGN KEY($COLUMN_STUDENT_ID) REFERENCES $TABLE_STUDENT($COLUMN_STUDENT_ID) ON DELETE CASCADE, "
            + "FOREIGN KEY($COLUMN_TUTOR_ID) REFERENCES $TABLE_TUTOR($COLUMN_TUTOR_ID) ON DELETE CASCADE)")

    private val createAdminTable = ("CREATE TABLE $TABLE_ADMIN ("
            + "$COLUMN_ADMIN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_ADMIN_EMAIL TEXT, "
            + "$COLUMN_ADMIN_PASSWORD TEXT)")

    private val createAvailabilityTable = ("CREATE TABLE $TABLE_AVAILABILITY ("
            + "$COLUMN_AVAILABILITY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "$COLUMN_TUTOR_ID INTEGER, "
            + "$COLUMN_DAY TEXT, "
            + "$COLUMN_IS_MORNING_AVAILABLE INTEGER, "
            + "$COLUMN_IS_EVENING_AVAILABLE INTEGER, "
            + "FOREIGN KEY($COLUMN_TUTOR_ID) REFERENCES tutor(tutorID)"
            + ")"
            )


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")
        db?.execSQL(createStudentTable)
        db?.execSQL(createTutorTable)
        db?.execSQL(createSubjectTable)
        db?.execSQL(createTeachTable)
        db?.execSQL(createMeetingTable)
        db?.execSQL(createAdminTable)
        db?.execSQL(createAvailabilityTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion){
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_TUTOR")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_SUBJECT")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_TEACH")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_MEETING")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_AVAILABILITY")

            onCreate(db)
        }

    }

    fun clearAllTables() {
        val db = this.writableDatabase
        try {
            db.beginTransaction()

            db.execSQL("DELETE FROM $TABLE_MEETING")
            db.execSQL("DELETE FROM $TABLE_TEACH")
            db.execSQL("DELETE FROM $TABLE_AVAILABILITY")
            db.execSQL("DELETE FROM $TABLE_STUDENT")
            db.execSQL("DELETE FROM $TABLE_TUTOR")
            db.execSQL("DELETE FROM $TABLE_SUBJECT")
            db.execSQL("DELETE FROM $TABLE_ADMIN")

            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$TABLE_MEETING'")
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$TABLE_TEACH'")
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$TABLE_AVAILABILITY'")
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$TABLE_STUDENT'")
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$TABLE_TUTOR'")
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$TABLE_SUBJECT'")
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='$TABLE_ADMIN'")

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error clearing tables: ${e.message}")
            throw e
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun clearAvailabilityForTutor(tutorId: Int): Boolean {
        val db = this.writableDatabase
        try {
            db.beginTransaction()

            // Delete only the availability records for the current tutor
            db.execSQL("DELETE FROM $TABLE_AVAILABILITY WHERE tutorID = ?", arrayOf(tutorId.toString()))

            // Optionally reset the auto-increment sequence for the availability table
            db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name = '$TABLE_AVAILABILITY'")

            db.setTransactionSuccessful()
            Log.d("DatabaseHelper", "Successfully cleared availability for tutorID: $tutorId")
            return true
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error clearing availability for tutorID $tutorId: ${e.message}", e)
            return false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun insertStudent(firstName: String, lastName: String, phoneNumber: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val hashedPassword = hashPassword(password)
        val contentValues = ContentValues().apply {
            put(COLUMN_STUDENT_FIRST_NAME, firstName)
            put(COLUMN_STUDENT_LAST_NAME, lastName)
            put(COLUMN_STUDENT_PHONE_NUMBER, phoneNumber)
            put(COLUMN_STUDENT_EMAIL, email)
            put(COLUMN_STUDENT_PASSWORD, hashedPassword)
        }
        val result = db.insert(TABLE_STUDENT, null, contentValues)
        db.close()
        return result
    }

    fun insertTutor(firstName: String, lastName: String, phoneNumber: String, email: String, password: String, approved: String): Long {
        val db = this.writableDatabase
        val hashedPassword = hashPassword(password)
        val contentValues = ContentValues().apply {
            put(COLUMN_TUTOR_FIRST_NAME, firstName)
            put(COLUMN_TUTOR_LAST_NAME, lastName)
            put(COLUMN_TUTOR_PHONE_NUMBER, phoneNumber)
            put(COLUMN_TUTOR_EMAIL, email)
            put(COLUMN_TUTOR_PASSWORD, hashedPassword)
            put(COLUMN_TUTOR_APPROVED, approved)
        }
        val result = db.insert(TABLE_TUTOR, null, contentValues)
        db.close()
        return result
    }

    fun insertSubject(name: String, description: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DESCRIPTION, description)
        }
        val result = db.insert(TABLE_SUBJECT, null, contentValues)
        Log.d("DatabaseHelper", "Inserted tutor with ID: $result")
        Log.d("DatabaseHelper", "Inserted subject: $name with result: $result")
        db.close()
        return result
    }

    fun insertTeach(tutorId: Long, subjectId: Int, qualificationLevel: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TUTOR_ID, tutorId)
            put(COLUMN_SUBJECT_ID, subjectId)
            put(COLUMN_QUALIFICATION_LEVEL, qualificationLevel)
        }
        val result = db.insert(TABLE_TEACH, null, contentValues)
        if (result == -1L) {
            Log.e("DatabaseHelper", "Insertion failed for tutorId: $tutorId, subjectId: $subjectId")
        } else {
            Log.d("DatabaseHelper", "Insertion succeeded with rowId: $result")
        }
        db.close()
        return result

    }

    fun insertAdmin(email: String, password: String): Long {
        val db = this.writableDatabase
        val hashedPassword = hashPassword(password)
        val contentValues = ContentValues().apply {
            put(COLUMN_ADMIN_EMAIL, email)
            put(COLUMN_ADMIN_PASSWORD, hashedPassword)
        }
        val result = db.insert(TABLE_ADMIN, null, contentValues)
        db.close()
        return result
    }

    fun insertMeeting(studentId: Int, tutorId: Int, date: String, startTime: String, endTime: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_STUDENT_ID, studentId)
            put(COLUMN_TUTOR_ID, tutorId)
            put(COLUMN_DATE, date)
            put(COLUMN_START_TIME, startTime)
            put(COLUMN_END_TIME, endTime)
        }
        val result = db.insert(TABLE_MEETING, null, contentValues)
        db.close()
        return result
    }

    fun getAllStudents(): List<Student> {
        val studentList = mutableListOf<Student>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENT", null)

        if (cursor.moveToFirst()) {
            do{
                val studentID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
                val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_STUDENT_LAST_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_STUDENT_PHONE_NUMBER))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_EMAIL))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_PASSWORD))

                studentList.add(Student(studentID, firstName, lastName, phoneNumber,email, password))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return studentList
    }

    fun getAllTutors(): List<Tutor> {
        val tutorList = mutableListOf<Tutor>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TUTOR", null)

        if (cursor.moveToFirst()) {
            do{
                val tutorID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_ID))
                val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_TUTOR_LAST_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_TUTOR_PHONE_NUMBER))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_EMAIL))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_PASSWORD))
                val approved = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_APPROVED))

                tutorList.add(Tutor(tutorID, firstName, lastName, phoneNumber,email, password, approved))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tutorList
    }

    fun getAllAdmins(): List<Admin> {
        val adminList = mutableListOf<Admin>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ADMIN", null)

        if (cursor.moveToFirst()) {
            do {
                val adminID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_EMAIL))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_ADMIN_PASSWORD))
                adminList.add(Admin(adminID, email, password))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return adminList
    }

    fun getAllSubjects(): List<Subject> {
        val subjectList = mutableListOf<Subject>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_SUBJECT", null)

        if (cursor.moveToFirst()) {
            do {
                val subjectID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                subjectList.add(Subject(subjectID, name, description))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return subjectList
    }

    fun getAllTeach(): List<Teach> {
        val teachList = mutableListOf<Teach>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TEACH", null)

        if (cursor.moveToFirst()) {
            do {
                val teachID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEACH_ID))
                val tutorID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_ID))
                val subjectID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT_ID))
                val qualificationLevel = cursor.getString(cursor.getColumnIndexOrThrow(
                    COLUMN_QUALIFICATION_LEVEL))

                teachList.add(Teach(teachID, tutorID, subjectID, qualificationLevel))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return teachList
    }

    fun getAllMeetings(): List<Meeting> {
        val meetingList = mutableListOf<Meeting>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MEETING", null)

        if (cursor.moveToFirst()) {
            do {
                val meetingID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MEETING_ID))
                val studentID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
                val tutorID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_ID))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME))
                val endTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_TIME))
                meetingList.add(Meeting(meetingID, studentID, tutorID, date, startTime, endTime))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return meetingList
    }
    fun findTutorById(tutorId: Int): Tutor {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TUTOR,
            null,
            "$COLUMN_TUTOR_ID = ?",
            arrayOf(tutorId.toString()),
            null,
            null,
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_ID))
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_LAST_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_PHONE_NUMBER))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_PASSWORD))
            val approved = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_APPROVED))

            cursor.close()
            db.close()
            return Tutor(id, firstName, lastName, phoneNumber, email, password, approved)
        }

        cursor.close()
        db.close()
        throw Exception("Tutor with ID $tutorId not found.")
    }

    fun findStudentById(studentId: Int): Student {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_STUDENT,
            null,
            "$COLUMN_STUDENT_ID = ?",
            arrayOf(studentId.toString()),
            null,
            null,
            null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_LAST_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_PHONE_NUMBER))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_PASSWORD))

            cursor.close()
            db.close()
            return Student(id, firstName, lastName, phoneNumber, email, password)
        }

        cursor.close()
        db.close()
        throw Exception("Student with ID $studentId not found.")
    }

    fun isTutor(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM TUTOR WHERE email = ?", arrayOf(username))
        if (cursor.moveToFirst()) {
            val storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_PASSWORD))
            val isValidPassword = BCrypt.checkpw(password, storedHashedPassword)
            cursor.close()
            return isValidPassword
        }
        cursor.close()
        return false
    }

    fun isStudent(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM STUDENT WHERE email = ?", arrayOf(username))
        if (cursor.moveToFirst()) {
            val storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_PASSWORD))
            val isValidPassword = BCrypt.checkpw(password, storedHashedPassword)
            cursor.close()
            return isValidPassword
        }
        cursor.close()
        return false
    }

    fun insertAvailability(tutorID: Int, day: String, isMorningAvailable: Boolean, isEveningAvailable: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TUTOR_ID, tutorID)
            put(COLUMN_DAY, day)
            put(COLUMN_IS_MORNING_AVAILABLE, if (isMorningAvailable) 1 else 0)
            put(COLUMN_IS_EVENING_AVAILABLE, if (isEveningAvailable) 1 else 0)
        }
        db.insert(TABLE_AVAILABILITY, null, values)
        db.close()
    }


    fun getAvailabilityByTutor(tutorID: Int): List<Availability> {
        val availabilityList = mutableListOf<Availability>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_AVAILABILITY,
            null,
            "$COLUMN_TUTOR_ID = ?",
            arrayOf(tutorID.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val availability = Availability(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVAILABILITY_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_MORNING_AVAILABLE)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_EVENING_AVAILABLE)) == 1
                )
                availabilityList.add(availability)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return availabilityList
    }

    fun findTutorByUsername(username: String): Tutor? {
        val db = this.readableDatabase
        var tutor: Tutor? = null
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TUTOR WHERE $COLUMN_TUTOR_EMAIL = ?", arrayOf(username))

        if (cursor.moveToFirst()) {
            val tutorID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_ID))
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_FIRST_NAME))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_LAST_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_PHONE_NUMBER))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_EMAIL))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_PASSWORD))
            val isApproved = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_APPROVED))

            tutor = Tutor(tutorID, firstName, lastName, phoneNumber, email, password, isApproved)
        }
        cursor.close()
        db.close()
        return tutor
    }

    fun findTutorsByAvailability(date: String, timeSlot: String, subject: String): List<TutorWithQualification> {
        val db = this.readableDatabase
        val tutorsWithQualification = mutableListOf<TutorWithQualification>()

        val cursor = db.rawQuery(
            """
        SELECT tutor.*, teach.qualificationLevel
        FROM tutor 
        JOIN availability ON tutor.tutorID = availability.tutorID
        JOIN teach ON tutor.tutorID = teach.tutorID
        JOIN subject ON teach.subjectID = subject.subjectID
        WHERE availability.day = ? 
        AND (
            (availability.isMorningAvailable = 1 AND ? = 'Morning') OR
            (availability.isEveningAvailable = 1 AND ? = 'Evening')
        )
        AND subject.name = ?
        """,
            arrayOf(date, timeSlot, timeSlot, subject) // Make sure timeSlot is passed correctly
        )

        while (cursor.moveToNext()) {
            val tutor = Tutor(
                tutorID = cursor.getInt(cursor.getColumnIndexOrThrow("tutorID")),
                firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName")),
                lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName")),
                phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                approved = cursor.getString(cursor.getColumnIndexOrThrow("approval"))
            )

            val qualification = cursor.getString(cursor.getColumnIndexOrThrow("qualificationLevel"))
            tutorsWithQualification.add(TutorWithQualification(tutor, qualification))
        }

        cursor.close()
        db.close()
        return tutorsWithQualification
    }

    fun updateTutorApprovalStatus(tutorId: Int, approved: Boolean) {
        val db = this.writableDatabase
        if (approved) {
            val values = ContentValues().apply {
                put(COLUMN_TUTOR_APPROVED, "true")
            }
            db.update(TABLE_TUTOR, values, "$COLUMN_TUTOR_ID = ?", arrayOf(tutorId.toString()))
        } else {
            db.delete(TABLE_TUTOR, "$COLUMN_TUTOR_ID = ?", arrayOf(tutorId.toString()))
        }
        db.close()
    }

    fun getTutorQualification(tutorID: Int, subject: String): String? {
        val db = this.readableDatabase
        var qualification: String? = null
        val cursor = db.rawQuery(
            """
        SELECT teach.qualification 
        FROM teach 
        WHERE teach.tutorID = ? 
        AND teach.subject = ?
        """,
            arrayOf(tutorID.toString(), subject)
        )

        if (cursor.moveToFirst()) {
            qualification = cursor.getString(cursor.getColumnIndexOrThrow("qualification"))
        }

        cursor.close()
        db.close()
        return qualification
    }

    fun findStudentByUsername(username: String): Student? {
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_STUDENT,
            arrayOf(COLUMN_STUDENT_ID, COLUMN_STUDENT_FIRST_NAME, COLUMN_STUDENT_LAST_NAME, COLUMN_STUDENT_PHONE_NUMBER, COLUMN_STUDENT_EMAIL, COLUMN_STUDENT_PASSWORD), // Columns to retrieve
            "$COLUMN_STUDENT_EMAIL = ?",
            arrayOf(username),
            null, // Group by
            null, // Having
            null // Order by
        )

        return if (cursor != null && cursor.moveToFirst()) {

            val studentID = cursor.getInt(cursor.getColumnIndexOrThrow("studentID"))
            val firstName = cursor.getString(cursor.getColumnIndexOrThrow("firstName"))
            val lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastName"))
            val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            cursor.close()

            Student(studentID, firstName, lastName, phoneNumber, email, password)
        } else {
            cursor?.close()
            null
        }
    }

    //fun insertDefaultSubjects() {
       // val db = writableDatabase
       // db.execSQL("INSERT INTO SUBJECT (name) VALUES ('Mathematics')")
       // db.execSQL("INSERT INTO SUBJECT (name) VALUES ('English')")
      //  db.execSQL("INSERT INTO SUBJECT (name) VALUES ('Science')")
   // }

    fun logAllTutorsData() {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT tutor.*, teach.qualificationLevel, subject.name AS subjectName, availability.day,
        availability.isMorningAvailable, availability.isEveningAvailable
        FROM tutor
        LEFT JOIN availability ON tutor.tutorID = availability.tutorID
        LEFT JOIN teach ON tutor.tutorID = teach.tutorID
        LEFT JOIN subject ON teach.subjectID = subject.subjectID
        """, null
        )

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val tutorName = cursor.getString(cursor.getColumnIndexOrThrow("firstName")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("lastName"))
                val qualification = cursor.getString(cursor.getColumnIndexOrThrow("qualificationLevel"))
                val subjectName = cursor.getString(cursor.getColumnIndexOrThrow("subjectName"))
                val day = cursor.getString(cursor.getColumnIndexOrThrow("day"))
                val isMorning = cursor.getInt(cursor.getColumnIndexOrThrow("isMorningAvailable")) == 1
                val isEvening = cursor.getInt(cursor.getColumnIndexOrThrow("isEveningAvailable")) == 1

                Log.d(
                    "DatabaseContent",
                    "Tutor: $tutorName, Subject: $subjectName, Qualification: $qualification, " +
                            "Day: $day, Morning: $isMorning, Evening: $isEvening"
                )
            }
        } else {
            Log.d("DatabaseContent", "No data found in the database.")
        }
        cursor.close()
        db.close()
    }

    fun insertSubjects() {
        val db = writableDatabase
        db.execSQL("INSERT INTO $TABLE_SUBJECT ($COLUMN_NAME, $COLUMN_DESCRIPTION) VALUES ('Mathematics', 'Mathematics subject description')")
        db.execSQL("INSERT INTO $TABLE_SUBJECT ($COLUMN_NAME, $COLUMN_DESCRIPTION) VALUES ('English', 'English subject description')")
        db.execSQL("INSERT INTO $TABLE_SUBJECT ($COLUMN_NAME, $COLUMN_DESCRIPTION) VALUES ('Science', 'Science subject description')")
    }

    fun logAllSubjects() {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_SUBJECT, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            Log.d("DatabaseContent", "Subject ID: $id, Name: $name")
        }
        cursor.close()
    }

    fun deleteMeetingById(meetingId: Int): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete(TABLE_MEETING, "$COLUMN_MEETING_ID = ?", arrayOf(meetingId.toString()))
        db.close()
        return rowsDeleted
    }


    fun isSlotAvailable(tutorId: Int, date: String, timeSlot: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_MEETING,
            null,
            "$COLUMN_TUTOR_ID = ? AND $COLUMN_DATE = ? AND ($COLUMN_START_TIME = ? OR $COLUMN_END_TIME = ?)",
            arrayOf(tutorId.toString(), date, timeSlot, timeSlot),
            null, null, null
        )
        // no rows = the slot is available
        val isSlotAvailable = cursor.count == 0
        cursor.close()
        db.close()
        return isSlotAvailable
    }

    fun bookMeetingIfAvailable(tutorId: Int, date: String, timeSlot: String, studentId: Int): String {
        return if (isSlotAvailable(tutorId, date, timeSlot)) {
            insertMeeting(studentId, tutorId, date, timeSlot, timeSlot)
            "Meeting booked successfully!"
        } else {
            "Sorry, the tutor is already booked at this time."
        }
    }

    // bookMeetingIfAvailable but it returns a Boolean, sometimes must use this based on expected return
    fun bookMeeting(tutorId: Int, date: String, timeSlot: String, studentId: Int): Boolean {
        return when (val resultMessage = bookMeetingIfAvailable(tutorId, date, timeSlot, studentId)) {
            "Meeting booked successfully!" -> true
            else -> false
        }
    }

    fun getStudentEmailFromID(sID: Int): String? {
        val db = this.readableDatabase
        var email: String? = null
        val query = "SELECT email FROM $TABLE_STUDENT WHERE studentID = ?"
        val cursor = db.rawQuery(query, arrayOf(sID.toString()))
        try {
            if (cursor.moveToFirst()) {
                email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
            db.close()
        }
        return email
    }





}

