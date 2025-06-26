package com.example.formsetu.util

import com.example.formsetu.data.model.FormSchema

object OcrFieldMapper {

    fun mapOcrTextToFormFields(ocrText: String, schema: FormSchema): Map<String, String> {
        val map = mutableMapOf<String, String>()

        val lines = ocrText.split("\n")
        for (line in lines) {
            val cleaned = line.trim()

            schema.fields.forEach { field ->
                val key = field.ocr_key ?: return@forEach
                when (key) {
                    "name" -> if (cleaned.contains("Name", ignoreCase = true)) {
                        map[field.key] = cleaned.replace("Name", "", ignoreCase = true).trim()
                    }
                    "dob" -> if (cleaned.contains(Regex("\\d{2}[/-]\\d{2}[/-]\\d{4}"))) {
                        map[field.key] = Regex("\\d{2}[/-]\\d{2}[/-]\\d{4}")
                            .find(cleaned)?.value ?: ""
                    }
                    "gender" -> if (cleaned.contains("Male", true) || cleaned.contains("Female", true)) {
                        map[field.key] = if (cleaned.contains("Female", true)) "Female" else "Male"
                    }
                    "aadhaar" -> if (cleaned.replace(" ", "").matches(Regex("\\d{12}"))) {
                        map[field.key] = cleaned.replace(" ", "")
                    }
                }
            }
        }

        return map
    }
}
