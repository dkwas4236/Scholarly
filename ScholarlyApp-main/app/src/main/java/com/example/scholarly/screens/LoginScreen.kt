package com.example.scholarly.screens

import android.provider.CalendarContract.Colors
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scholarly.viewmodel.UserViewModel
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.example.scholarly.MainActivity

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var errorMessage = remember { mutableStateOf("") }
    var usernameError = remember { mutableStateOf("") }

    val focusRequesterPassword = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            usernameError.value = "Email is required"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameError.value = "Invalid email format"
            false
        } else {
            usernameError.value = ""
            true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MainActivity.ColorStorage.backgroundPurple
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", color = Color.White, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(32.dp))

            // Username input field
            TextField(
                value = username.value,
                onValueChange = {
                    username.value = it
                    validateEmail(it) },
                label = { Text("Username") },
                singleLine = true,
                isError = usernameError.value.isNotEmpty(),
                supportingText = {
                    if (usernameError.value.isNotEmpty()) {
                        Text(
                            text = usernameError.value,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterPassword.requestFocus() })
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input field
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                modifier = Modifier.focusRequester(focusRequesterPassword)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            ElevatedButton(onClick = {
                // Specific credentials for tutor
                if (username.value == "tutor" && password.value == "password") {
                    navController.navigate("tutor")  // Navigate to the tutor page
                } else {
                    val userType = userViewModel.verifyUserType(username.value, password.value)
                    if (userType == "admin") {
                        userViewModel.loadTutors()
                        userViewModel.loadStudents()
                    }
                    when (userType) {
                        "admin" -> navController.navigate("users")
                        "tutor" -> {
                            if (!validateEmail(username.value)) {
                                return@ElevatedButton
                            }
                            val tutor = userViewModel.databaseHelper.findTutorByUsername(username.value)
                            tutor?.let {
                                userViewModel.setCurrentUserId(it.tutorID)  // Set tutor ID
                                navController.navigate("tutor")  // Navigate to the tutor welcome page
                            } ?: run {
                                errorMessage.value = "Error retrieving tutor information."
                            }
                        }
                        "student" -> {
                            if (!validateEmail(username.value)) {
                                return@ElevatedButton
                            }
                            val student = userViewModel.databaseHelper.findStudentByUsername(username.value) // Fetch the student details
                            student?.let {
                                userViewModel.setCurrentUserId(it.studentID) // Set student ID
                                navController.navigate("student") // Navigate to the student welcome page
                            } ?: run {
                                errorMessage.value = "Error retrieving student information."
                            }
                        }
                        else -> errorMessage.value = "Invalid username or password"
                    }
                }
            }) {
                Text("Login", color = MainActivity.ColorStorage.textPurple)
            }

            Spacer(modifier = Modifier.height(16.dp))

            FilledTonalButton(onClick = { navController.navigate("signup") },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt,
                    contentColor = MainActivity.ColorStorage.textPurple
                )
            ) {
                Text("No Account? Sign up now!")
            }

            // Display an error message if there is one
            if (errorMessage.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = errorMessage.value, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
