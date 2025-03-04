package com.example.scholarly.screens

import android.provider.CalendarContract.Colors
import android.provider.ContactsContract.Data
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.scholarly.MainActivity
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.viewmodel.UserInsertModel
import android.util.Patterns
import java.util.regex.Pattern

@Composable
fun SignupScreen(navController: NavController, userInsertModel: UserInsertModel) {
    // Variables to store credentials
    var firstName = remember { mutableStateOf("") }
    var lastName = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var phoneNumber = remember { mutableStateOf("") }
    var userType = remember { mutableStateOf("student") } // Default to student
    var errorMessage = remember { mutableStateOf("") }

    var emailError = remember { mutableStateOf("") }
    var phoneError = remember { mutableStateOf("") }
    var passwordError = remember { mutableStateOf("") }

    // Focus requesters for form fields
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val focusRequesterConfirmPassword = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Function to validate email
    fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            emailError.value = "Email is required"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.value = "Invalid email format"
            false
        } else {
            emailError.value = ""
            true
        }
    }

    // Function to validate phone number
    fun validatePhoneNumber(phone: String): Boolean {
        return if (phone.isEmpty()) {
            phoneError.value = "Phone number is required"
            false
        } else if (!Pattern.matches("^\\d{10}$", phone)) {
            phoneError.value = "Phone number must be 10 digits"
            false
        } else {
            phoneError.value = ""
            true
        }
    }

    // Function to validate passwords
    fun validatePasswords(pass: String, confirmPass: String): Boolean {
        return if (pass.isEmpty()) {
            passwordError.value = "Password is required"
            false
        } else if (pass != confirmPass) {
            passwordError.value = "Passwords do not match"
            false
        } else {
            passwordError.value = ""
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // First name field
            TextField(
                value = firstName.value,
                onValueChange = { firstName.value = it },
                label = { Text("First Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesterEmail.requestFocus() }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last name field
            TextField(
                value = lastName.value,
                onValueChange = { lastName.value = it },
                label = { Text("Last Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesterEmail.requestFocus() }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            TextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                    validateEmail(it) },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError.value.isNotEmpty(),
                supportingText = {
                    if (emailError.value.isNotEmpty()) {
                        Text(
                            text = emailError.value,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesterPassword.requestFocus() }
                ),
                modifier = Modifier.focusRequester(focusRequesterEmail)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone number field
            TextField(
                value = phoneNumber.value,
                onValueChange = {
                    phoneNumber.value = it.filter { char -> char.isDigit() }
                    validatePhoneNumber(phoneNumber.value) },
                label = { Text("Phone Number") },
                singleLine = true,
                isError = phoneError.value.isNotEmpty(),
                supportingText = {
                    if (phoneError.value.isNotEmpty()) {
                        Text(
                            text = phoneError.value,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesterEmail.requestFocus() }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            TextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                    validatePasswords(it, confirmPassword.value) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = passwordError.value.isNotEmpty(),
                supportingText = {
                    if (passwordError.value.isNotEmpty()) {
                        Text(
                            text = passwordError.value,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequesterConfirmPassword.requestFocus() }
                ),
                modifier = Modifier.focusRequester(focusRequesterPassword)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password field
            // Not sure if we need this, may delete later
            TextField(
                value = confirmPassword.value,
                onValueChange = {
                    confirmPassword.value = it
                    validatePasswords(password.value, it) },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = passwordError.value.isNotEmpty(),
                supportingText = {
                    if (passwordError.value.isNotEmpty()) {
                        Text(
                            text = passwordError.value,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier.focusRequester(focusRequesterConfirmPassword)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Type Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("I am a:", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Row {
                    RadioButton(
                        selected = userType.value == "student",
                        onClick = { userType.value = "student" },
                        colors = RadioButtonColors(
                            selectedColor = Color.White,
                            unselectedColor = Color.DarkGray,
                            disabledSelectedColor = Color.Unspecified,
                            disabledUnselectedColor = Color.Unspecified
                        )
                    )
                    Text(
                        "Student",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp, top = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = userType.value == "tutor",
                        onClick = { userType.value = "tutor" },
                        colors = RadioButtonColors(
                            selectedColor = Color.White,
                            unselectedColor = Color.DarkGray,
                            disabledSelectedColor = Color.Unspecified,
                            disabledUnselectedColor = Color.Unspecified
                        )
                    )
                    Text(
                        "Tutor",
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp, top = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button

            ElevatedButton(
                onClick = {
                    val isEmailValid = validateEmail(email.value)
                    val isPhoneValid = validatePhoneNumber(phoneNumber.value)
                    val isPasswordValid = validatePasswords(password.value, confirmPassword.value)

                    if (isEmailValid && isPhoneValid && isPasswordValid) {
                        if (userType.value == "student") {
                            userInsertModel.insertStudent(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                phoneNumber = phoneNumber.value,
                                email = email.value,
                                password = password.value
                            )
                            navController.navigate("login")
                        }
                        else {
                            val newTutorId = userInsertModel.insertTutor(
                                firstName = firstName.value,
                                lastName = lastName.value,
                                phoneNumber = phoneNumber.value,
                                email = email.value,
                                password = password.value,
                                approved = "false"
                            )
                            navController.navigate("credentials/$newTutorId")
                        }
                    }
                }
            ) {
                Text("Sign Up", color = MainActivity.ColorStorage.textPurple)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Return to LoginScreen Button
            FilledTonalButton(
                onClick = { navController.navigateUp() },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MainActivity.ColorStorage.backgroundPurpleAlt,
                    contentColor = MainActivity.ColorStorage.textPurple
                )
            ) {
                Text("Already have an account? Login")
            }
        }
    }
}