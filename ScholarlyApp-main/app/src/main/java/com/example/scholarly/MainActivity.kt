package com.example.scholarly

import AvailabilityScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.screens.*
import com.example.scholarly.ui.theme.ScholarlyTheme
import com.example.scholarly.viewmodel.MeetingsViewModel
import com.example.scholarly.viewmodel.UserViewModel
import com.example.scholarly.viewmodel.UserInsertModel
import com.example.scholarly.viewmodel.TutorCredentialsInsertModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument


class MainActivity : ComponentActivity() {

    // Companion object to store color constants
    companion object ColorStorage {
        val backgroundPurple = Color(0xFF65558F)
        val backgroundPurpleAlt = Color(0xFFD0BCFE)
        val iconPurple = Color(0xFF6A4C93)
        val textPurple = Color(0xFF4E3586)
        val listBackgroundGrey = Color(0xFF141218)
        val searchBarBackgroundGrey = Color(0xFF2b2930)
        val denyRed = Color(0xFFE78284)
        val approveGreen = Color(0XFFA6D189)
        val whiteText = Color(0xFFFFFFFF)

    }

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userViewModel: UserViewModel
    private lateinit var userInsertModel: UserInsertModel
    private lateinit var tutorCredentialsInsertModel: TutorCredentialsInsertModel

    // Function to populate DB in case it's empty
    private fun populateDB() {
        //Insert Students
        databaseHelper.insertStudent("Jay", "Bhatt", "780-000-0000",
            "jayb@gmail.com", "123")
        databaseHelper.insertStudent("Darion", "Kwasnitza", "780-000-0000",
            "darionk@gmail.com", "123")
        databaseHelper.insertStudent("Mustafa", "Al-Hamadani", "780-000-0000",
            "mustafaah@gmail.com", "123")
        databaseHelper.insertStudent("Sanjay", "Dhanashekharan", "780-000-0000",
            "sanjayd@gmail.com", "123")
        databaseHelper.insertStudent("Sukhraj", "Baath", "780-000-0000",
            "sukhrajb@gmail.com", "123")

        //Insert Tutors
        databaseHelper.insertTutor("Altaf", "Khetani", "780-000-0000",
            "altafk@gmail.com", "123", "No")
        databaseHelper.insertTutor("Candy", "Pang", "780-000-0000",
            "candyp@gmail.com", "123", "No")
        databaseHelper.insertTutor("Hanan", "Selah", "780-000-0000",
            "hanans@gmail.com", "123", "No")

        //Insert Meetings
        databaseHelper.insertMeeting(1, 1,"2024-10-20",
            "12:00 PM", "01:00 PM")
        databaseHelper.insertMeeting(2, 1,"2024-10-22",
            "07:00 AM", "10:00 AM")
        databaseHelper.insertMeeting(3, 1,"2024-10-17",
            "06:00 PM", "08:00 PM")
    }

    private fun populateDB1() {
        //insert subjects
        databaseHelper.insertSubject("English", "Learning about language")
        databaseHelper.insertSubject("Mathematics", "Learning about numbers")
        databaseHelper.insertSubject("Science", "Learning about life")

        // insert teach
        databaseHelper.insertTeach(2,3,"PhD")
        databaseHelper.insertTeach(1, 3, "Masters")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Initialize the DatabaseHelper
        databaseHelper = DatabaseHelper(this)
        userViewModel = UserViewModel(databaseHelper)
        userInsertModel = UserInsertModel(databaseHelper)
        tutorCredentialsInsertModel = TutorCredentialsInsertModel(databaseHelper, userViewModel)

        //populateDB()
        //populateDB1()
        // UNCOMMENT THE LINE ABOVE IF DB IS NOT POPULATED, OR IF THE APP CRASHES ON STARTUP
        // ONLY RUN ONCE THEN UNCOMMENT, OTHERWISE DUPLICATE ENTRIES WILL APPEAR
        // (MAY NEED TO INCREMENT DB VERSION)


        setContent {
            ScholarlyTheme {
                val navController = rememberNavController()
                val bottomBarState = remember { mutableStateOf(true) }
                val meetingsViewModel: MeetingsViewModel =
                    viewModel { MeetingsViewModel(databaseHelper) }

                // Navigation host handles screen transitioning
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(navController, userViewModel)
                    }

                    composable("signup") {
                        SignupScreen(navController, userInsertModel)
                    }
                    composable("credentials/{tutorId}") { backStackEntry ->
                        val tutorId = backStackEntry.arguments?.getString("tutorId")?.toLongOrNull() ?: 0
                        TutorCredentialsScreen(navController, tutorId, tutorCredentialsInsertModel)
                    }
                    composable("users") {
                        UsersScreen(navController, userViewModel.tutors, userViewModel.students, bottomBarState)
                    }
                    composable("meetings") {
                        MeetingsScreen(meetingsViewModel, navController, bottomBarState)
                    }
                    composable("tutor") { TutorScreen(navController, meetingsViewModel, userViewModel, bottomBarState)
                    }
                    composable("student") { StudentScreen(navController, bottomBarState, meetingsViewModel, userViewModel)
                    }
                    composable("availability") {
                        AvailabilityScreen(userViewModel, databaseHelper)
                    }
                    composable("tutorDetails/{tutorID}") { backStackEntry ->
                        val tutorIDString = backStackEntry.arguments?.getString("tutorID") ?: "0"
                        val tutorID = tutorIDString.toIntOrNull() ?: 0
                        val tutor = databaseHelper.findTutorById(tutorID)
                        TutorDetailsScreen(navController, tutor)
                    }
                    composable("studentDetails/{studentID}") { backStackEntry ->
                        val studentIDString = backStackEntry.arguments?.getString("studentID") ?: "0"
                        val studentID = studentIDString.toIntOrNull() ?: 0
                        val student = databaseHelper.findStudentById(studentID)
                        StudentDetailsScreen(navController, student)
                    }
                    composable("bookMeeting") { BookMeeting(navController) }

                    composable("googleMeetCode") { GoogleMeetCodeScreen(navController) }

                    composable("bookingConfirmation/{selectedDate}/{selectedDayOfWeek}/{selectedTimeSlot}/{selectedSubject}") { backStackEntry ->
                        val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
                        val selectedDayOfWeek = backStackEntry.arguments?.getString("selectedDayOfWeek") ?: ""
                        val selectedTimeSlot = backStackEntry.arguments?.getString("selectedTimeSlot") ?: ""
                        val selectedSubject = backStackEntry.arguments?.getString("selectedSubject") ?: ""
                        BookingConfirmationScreen(navController, selectedDate, selectedDayOfWeek, selectedTimeSlot, selectedSubject, userViewModel)
                    }

                    composable("meetingDetails/{meetingID}") { backStackEntry ->
                        val meetingIDString = backStackEntry.arguments?.getString("meetingID") ?: "0"
                        val meetingID = meetingIDString.toIntOrNull() ?: 0
                        MeetingDetailsScreen(meetingID, meetingsViewModel, navController)
                    }
                    composable("approvals") { PendingTutorScreen(navController, bottomBarState, userViewModel) }
                        composable(
                            "PendingTutorDetails/{tutorId}",
                            arguments = listOf(navArgument("tutorId") { type = NavType.IntType })
                            ) { backStackEntry ->
                            val tutorId = backStackEntry.arguments?.getInt("tutorId") ?: return@composable
                            PendingTutorDetailsScreen(navController, tutorId, userViewModel)
                    }
                    composable("settings") { SettingsScreen(navController, userViewModel, bottomBarState) }
                    composable("settingsstudent") { SettingsScreenStudent(navController, userViewModel, bottomBarState) }
                    composable("settingstutor") { SettingsScreenTutor(navController, userViewModel, bottomBarState) }

                }
            }
        }
    }
}
