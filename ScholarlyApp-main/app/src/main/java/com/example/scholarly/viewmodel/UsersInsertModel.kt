package com.example.scholarly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scholarly.database.DatabaseHelper
import com.example.scholarly.models.Tutor
import com.example.scholarly.models.Student
import kotlinx.coroutines.launch

class UserInsertModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    // Update insertTutor to return the new tutor ID
    fun insertTutor(firstName: String, lastName: String, phoneNumber: String, email: String, password: String, approved: String): Long {
        var newTutorId: Long = -1 // Initialize with a default value
        viewModelScope.launch {
            newTutorId = databaseHelper.insertTutor(firstName, lastName, phoneNumber, email, password, approved)
        }
        return newTutorId
    }

    fun insertStudent(firstName: String, lastName: String, phoneNumber: String, email: String, password: String) {
        viewModelScope.launch {
            databaseHelper.insertStudent(firstName, lastName, phoneNumber, email, password)
        }
    }
}
