package com.example.scholarly.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.models.Tutor
import com.example.scholarly.models.Student
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape


@Composable
fun UsersScreen(navController: NavController, tutors: List<Tutor>, students: List<Student>, bottomBarState: MutableState<Boolean>) {

    // State for the search query
    var searchQuery by remember { mutableStateOf("") }

    // Filter tutors and students based on the search query
    val filteredTutors = tutors.filter { tutor ->
        "${tutor.firstName} ${tutor.lastName}".contains(searchQuery, ignoreCase = true) ||
                tutor.tutorID.toString().contains(searchQuery)
    }

    val filteredStudents = students.filter { student ->
        "${student.firstName} ${student.lastName}".contains(searchQuery, ignoreCase = true) ||
                student.studentID.toString().contains(searchQuery)
    }

    Scaffold(
        bottomBar = {
            if (bottomBarState.value) {
                BottomBar(navController, bottomBarState)
            }
        },
        content = { paddingValues ->
            // Surface as the background color fill
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Apply padding from the Scaffold
                color = MainActivity.listBackgroundGrey
            ) {

                // Column layout to arrange the search bar and the filtered list vertically
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Search bar at the top
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search by Name or ID", color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    // Display filtered results
                    LazyColumn(
                        modifier = Modifier
                            .background(MainActivity.ColorStorage.listBackgroundGrey)
                            .fillMaxSize()
                    ) {
                        // Tutor items
                        items(filteredTutors) { tutor ->
                            TutorItem(tutor, navController)
                        }

                        // Student items
                        items(filteredStudents) { student ->
                            StudentItem(student, navController)
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun TutorItem(tutor: Tutor, navController: NavController) {

    // box to display the tutor item with clickable behavior
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Add vertical space between items
            .clickable {
                // Navigate to tutor details or perform some action
                navController.navigate("tutorDetails/${tutor.tutorID}")
            }
            .background(Color.DarkGray, shape = RoundedCornerShape(12.dp)) // Box background and rounded corners
            .padding(16.dp) // Inner padding for the content
    ) {
        // Column to display the tutor's name
        Column {
            Text(
                text = "${tutor.firstName} ${tutor.lastName}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

@Composable
fun StudentItem(student: Student, navController: NavController) {
    //displays the student item with responsive clicking
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Add vertical space between items
            .clickable {
                // Navigate to student details or perform some action
                navController.navigate("studentDetails/${student.studentID}")
            }
            .background(Color.DarkGray, shape = RoundedCornerShape(12.dp)) // Box background and rounded corners
            .padding(16.dp) // Inner padding for the content
    ) {
        // Column to display the student name
        Column {
            Text(
                text = "${student.firstName} ${student.lastName}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White

            )
        }
    }
}
