package com.example.scholarly.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scholarly.database.DatabaseHelper
import kotlinx.coroutines.launch

class TutorCredentialsInsertModel(private val databaseHelper: DatabaseHelper, private val usersViewModel: UserViewModel) : ViewModel() {

    fun insertCredential(subjectId: Int, qualificationLevel: String, tutorID: Long) {
        viewModelScope.launch {
            if (tutorID != null) {
                Log.d("TutorCredentialsInsertModel", "Tutor ID: $tutorID, Inserting Credential")
                databaseHelper.insertTeach(tutorID, subjectId, qualificationLevel)
            } else {
                Log.e("TutorCredentialsInsertModel", "Tutor ID is null; cannot insert")
            }

        }
    }

}