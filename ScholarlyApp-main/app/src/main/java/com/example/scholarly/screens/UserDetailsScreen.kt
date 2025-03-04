package com.example.scholarly.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scholarly.MainActivity
import com.example.scholarly.models.Tutor
import com.example.scholarly.models.Student
import com.example.scholarly.ui.theme.darkGrayCustom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorDetailsScreen(navController: NavController, tutor: Tutor) {
    // this is the surface as the main container with a custom background color
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkGrayCustom
    ) {

        // Column to arrange the tutor details vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Top app bar with a title and a back navigation button
            TopAppBar(
                title = { Text("Tutor Details", color = Color.White, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MainActivity.ColorStorage.backgroundPurple)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Display tutor details, in white text
            Text(text = "ID: ${tutor.tutorID}", color = Color.White)
            Text(text = "First Name: ${tutor.firstName}", color = Color.White)
            Text(text = "Last Name: ${tutor.lastName}", color = Color.White)
            Text(text = "Email: ${tutor.email}", color = Color.White)
            Text(text = "Phone Number: ${tutor.phoneNumber}", color = Color.White)
            Text(text = "Password: ${tutor.password}", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailsScreen(navController: NavController, student: Student) {
    // surface as the main container with a custom background color like tutor
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkGrayCustom
    ) {

        // Column to arrange the student details vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Top app bar with a title and a back navigation button
            TopAppBar(
                title = { Text("Student Details", color = Color.White, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MainActivity.ColorStorage.backgroundPurple)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Display student details,  in white text
            Text(text = "ID: ${student.studentID}", color = Color.White)
            Text(text = "First Name: ${student.firstName}", color = Color.White)
            Text(text = "Last Name: ${student.lastName}", color = Color.White)
            Text(text = "Email: ${student.email}", color = Color.White)
            Text(text = "Phone Number: ${student.phoneNumber}", color = Color.White)
            Text(text = "Password: ${student.password}", color = Color.White)
        }
    }
}
