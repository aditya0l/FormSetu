package com.example.formsetu.util

import com.example.formsetu.data.model.FormSchema

object FormValidator {

    fun validate(
        schema: FormSchema,
        fieldStates: Map<String, String>,
        language: String = "hi"
    ): Map<String, String> {
        val errorMap = mutableMapOf<String, String>()

        for (field in schema.fields) {
            val value = fieldStates[field.key]?.trim() ?: ""
            val label = field.label[language] ?: field.key

            // 1. Required check
            if (field.required && value.isEmpty()) {
                errorMap[field.key] = when (language) {
                    "hi" -> "$label आवश्यक है।"
                    else -> "$label is required."
                }
                continue
            }

            // 2. minLength
            if (field.minLength != null && value.length < field.minLength) {
                errorMap[field.key] = when (language) {
                    "hi" -> "$label कम से कम ${field.minLength} अक्षरों का होना चाहिए।"
                    else -> "$label must be at least ${field.minLength} characters."
                }
                continue
            }

            // 3. maxLength
            if (field.maxLength != null && value.length > field.maxLength) {
                errorMap[field.key] = when (language) {
                    "hi" -> "$label ${field.maxLength} अक्षरों से अधिक नहीं हो सकता।"
                    else -> "$label must be at most ${field.maxLength} characters."
                }
                continue
            }

            // 4. Regex
            if (!value.isEmpty() && field.regex != null) {
                val pattern = field.regex.toRegex()
                if (!pattern.matches(value)) {
                    errorMap[field.key] = when (language) {
                        "hi" -> "$label का स्वरूप सही नहीं है।"
                        else -> "$label format is invalid."
                    }
                }
            }
        }

        return errorMap
    }
}
