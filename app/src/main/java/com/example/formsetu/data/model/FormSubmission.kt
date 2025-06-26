package com.example.formsetu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "form_submissions")
data class FormSubmission(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val formType: String, // e.g., "PAN", "Ration Card", "Passport"
    val submissionDate: Long, // System.currentTimeMillis()
    val formDataJson: String // Store the filled form as JSON string
) 