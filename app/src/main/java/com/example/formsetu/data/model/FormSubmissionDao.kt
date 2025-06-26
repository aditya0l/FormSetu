package com.example.formsetu.data.model

import androidx.room.*

@Dao
interface FormSubmissionDao {
    @Insert
    suspend fun insert(submission: FormSubmission): Long

    @Query("SELECT * FROM form_submissions WHERE formType = :formType ORDER BY submissionDate DESC")
    suspend fun getSubmissionsByType(formType: String): List<FormSubmission>

    @Query("SELECT * FROM form_submissions ORDER BY submissionDate DESC")
    suspend fun getAllSubmissions(): List<FormSubmission>
} 