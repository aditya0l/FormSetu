package com.example.formsetu.data.model

data class FormSchema(
    val title: Map<String, String>,            // Title in multiple languages (hi/en)
    val fields: List<FormField>                // List of fields in order
)

data class FormField(
    val type: String,                          // "text", "dropdown", "date"
    val key: String,                           // Unique field identifier
    val label: Map<String, String>,            // Field label in multiple languages
    val hint: Map<String, String>? = null,     // Placeholder or explanation
    val required: Boolean = false,             // Is this field required?
    val options: List<String>? = null,         // For dropdowns only
    val ai_guidance: Map<String, String>? = null,  // Tooltip/audio hint
    val ocr_key: String? = null,               // Mapped OCR key for autofill
    val visible_if: Condition? = null,         // Conditional visibility logic
    val regex: String? = null,                 // Optional validation regex
    val minLength: Int? = null,                // Optional: min characters
    val maxLength: Int? = null                 // Optional: max characters
)

data class Condition(
    val field: String,                         // Depends on this field
    val value: String                          // Show only if equals this
)
