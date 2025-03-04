// viewmodel/UserViewModel.kt
package com.example.scholarly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.models.Tutor
import com.example.scholarly.models.Student
import kotlinx.coroutines.launch

class UserViewModel(val databaseHelper: DatabaseHelper) : ViewModel() {

    var tutors: List<Tutor> = listOf()
    var unapprovedTutors: List<Tutor> = listOf()
    var students: List<Student> = listOf()

    private var currentUserId: Int? = null
    fun setCurrentUserId(id: Int?) {
        currentUserId = id
    }

    fun getCurrentUserId(): Int? {
        return currentUserId
    }

    fun loadTutors() {
        viewModelScope.launch {
            // Fetch tutors from the database
            tutors = databaseHelper.getAllTutors() // Example method to get tutors
        }
    }

    fun approveTutor(tutorId: Int) {
        viewModelScope.launch {
            databaseHelper.updateTutorApprovalStatus(tutorId, true)
            tutors = databaseHelper.getAllTutors()
        }
    }

    fun denyTutor(tutorId: Int) {
        viewModelScope.launch {
            databaseHelper.updateTutorApprovalStatus(tutorId, false)
            tutors = databaseHelper.getAllTutors()
        }
    }

    fun loadStudents() {
        viewModelScope.launch {
            // Fetch students from the database
            students = databaseHelper.getAllStudents() // Example method to get students
        }
    }
    fun verifyUserType(username: String, password: String): String {
        return when {
            username == "admin" && password == "password" -> "admin"
            username == "student" && password == "password" -> "student"
            databaseHelper.isTutor(username, password) -> "tutor"
            databaseHelper.isStudent(username, password) -> "student"
            else -> "none"
        }
    }

}


