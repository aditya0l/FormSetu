package com.example.formsetu.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.formsetu.data.model.FormField
import com.example.formsetu.data.model.FormSchema

class FormViewModel : ViewModel() {

    val fieldStates = mutableStateMapOf<String, String>()
    val errorStates = mutableStateMapOf<String, String?>()

    fun validateForm(schema: FormSchema, language: String = "en"): Boolean {
        var isValid = true
        errorStates.clear()

        for (field in schema.fields) {
            val value = fieldStates[field.key]?.trim()
            if (field.required && (value.isNullOrEmpty())) {
                errorStates[field.key] = "Required field"
                isValid = false
            }
            // More validation logic can go here (regex, min/max, etc.)
        }

        return isValid
    }

    fun updateField(key: String, value: String) {
        fieldStates[key] = value
        errorStates[key] = null
    }

    fun prefillFieldFromOCR(ocrMap: Map<String, String>) {
        ocrMap.forEach { (ocrKey, value) ->
            val matchedKey = fieldStates.keys.find { it == ocrKey }
            matchedKey?.let {
                fieldStates[it] = value
            }
        }
    }

    fun resetForm() {
        fieldStates.clear()
        errorStates.clear()
    }
}