package com.example.scholarly.models

data class Meeting(
    val meetingID: Int,
    val studentID: Int,
    val tutorID: Int,
    val date: String,
    val startTime: String,
    val endTime: String
)