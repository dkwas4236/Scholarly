package com.example.scholarly.models

data class Availability(
    val availabilityID: Int,
    val tutorID: Int,
    val day: String, // e.g., "Monday"
    val isMorningAvailable: Boolean,
    val isEveningAvailable: Boolean
)
