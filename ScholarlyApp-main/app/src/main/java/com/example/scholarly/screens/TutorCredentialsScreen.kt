package com.example.scholarly.screens

import androidx.compose.foundation.background
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
import com.example.scholarly.viewmodel.TutorCredentialsInsertModel

data class SubjectEntry(
    val subject: String,
    val educationLevel: String
)

@Composable
fun TutorCredentialsScreen(navController: NavController, tutorID: Long, tutorCredentialsInsertModel: TutorCredentialsInsertModel) {
    var subjects by remember { mutableStateOf(listOf<SubjectEntry>()) }
    var currentSubject by remember { mutableStateOf("") }
    var currentEducationLevel by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSubjectDropdown by remember { mutableStateOf(false) }
    var showEducationDropdown by remember { mutableStateOf(false) }

    val subjectOptions = listOf(
        "Math",
        "Science",
        "English"
    )

    val educationLevels = listOf(
        "High School Diploma",
        "Associate's Degree",
        "Bachelor's Degree",
        "Master's Degree",
        "Doctorate",
        "Professional Certification"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MainActivity.ColorStorage.backgroundPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Your Credentials",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Please add the subjects you can teach and your education level for each subject.",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(subjects) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MainActivity.ColorStorage.backgroundPurpleAlt
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = entry.subject,
                                    color = MainActivity.ColorStorage.textPurple,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = entry.educationLevel,
                                    color = MainActivity.ColorStorage.textPurple,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(
                                onClick = {
                                    subjects = subjects.filter { it != entry }
                                }
                            ) {
                                Text("×", color = MainActivity.ColorStorage.textPurple)
                            }
                        }
                    }
                }
            }

            ElevatedButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Subject", color = MainActivity.ColorStorage.textPurple)
            }

            Spacer(modifier = Modifier.height(16.dp))

            FilledTonalButton(
                onClick = {
                    if (subjects.isEmpty()) {
                        errorMessage = "Please add at least one subject"
                    } else {
                        subjects.forEach { entry ->
                            val subjectID = when (entry.subject) {
                                "Math" -> 1
                                "Science" -> 3
                                "English" -> 2
                                else -> 0
                            }


                            // insertCredential for each entry in the subjects list
                            tutorCredentialsInsertModel.insertCredential(
                                subjectID,
                                entry.educationLevel,
                                tutorID
                            )
                        }
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt,
                    contentColor = MainActivity.ColorStorage.textPurple
                )
            ) {
                Text("Done")
            }


            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = {
                    Text(
                        "Add Subject",
                        style = MaterialTheme.typography.titleLarge,
                        color = MainActivity.ColorStorage.whiteText
                    )
                },
                text = {
                    Column {
                        // Subject Selection
                        Box {
                            Button(
                                onClick = { showSubjectDropdown = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt,
                                    contentColor = MainActivity.ColorStorage.textPurple
                                )
                            ) {
                                Text(
                                    currentSubject.ifEmpty { "Select Subject" }
                                )
                            }

                            DropdownMenu(
                                expanded = showSubjectDropdown,
                                onDismissRequest = { showSubjectDropdown = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .background(MainActivity.ColorStorage.backgroundPurpleAlt)
                            ) {
                                subjectOptions.forEach { subject ->
                                    DropdownMenuItem(
                                        text = { Text(subject, color = MainActivity.ColorStorage.textPurple) },
                                        onClick = {
                                            currentSubject = subject
                                            showSubjectDropdown = false
                                        },
                                        colors = MenuDefaults.itemColors(
                                            textColor = MainActivity.ColorStorage.textPurple
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Education Level Selection
                        Box {
                            Button(
                                onClick = { showEducationDropdown = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt,
                                    contentColor = MainActivity.ColorStorage.textPurple
                                )
                            ) {
                                Text(
                                    currentEducationLevel.ifEmpty { "Select Education Level" }
                                )
                            }

                            DropdownMenu(
                                expanded = showEducationDropdown,
                                onDismissRequest = { showEducationDropdown = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .background(MainActivity.ColorStorage.backgroundPurpleAlt)
                            ) {
                                educationLevels.forEach { level ->
                                    DropdownMenuItem(
                                        text = { Text(level, color = MainActivity.ColorStorage.textPurple) },
                                        onClick = {
                                            currentEducationLevel = level
                                            showEducationDropdown = false
                                        },
                                        colors = MenuDefaults.itemColors(
                                            textColor = MainActivity.ColorStorage.textPurple
                                        )
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (currentSubject.isNotEmpty() && currentEducationLevel.isNotEmpty()) {
                                subjects =
                                    subjects + SubjectEntry(currentSubject, currentEducationLevel)
                                currentSubject = ""
                                currentEducationLevel = ""
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MainActivity.ColorStorage.whiteText
                        )
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showAddDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MainActivity.ColorStorage.whiteText
                        )
                    ) {
                        Text("Cancel")
                    }
                },
                containerColor = MainActivity.ColorStorage.backgroundPurple,
                titleContentColor = MainActivity.ColorStorage.textPurple,
                textContentColor = Color.White
            )
        }
    }
}