package com.example.formsetu.data.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FormSubmission::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun formSubmissionDao(): FormSubmissionDao
} 