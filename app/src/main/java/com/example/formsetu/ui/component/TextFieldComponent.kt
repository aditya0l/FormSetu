package com.example.formsetu.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldComponent(
    value: String,
    label: String,
    hint: String?,
    isError: Boolean,
    errorText: String?,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { if (hint != null) Text(hint) },
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (isError && errorText != null) {
            Text(
                text = errorText, 
                color = MaterialTheme.colorScheme.error, 
                fontSize = MaterialTheme.typography.labelSmall.fontSize
            )
        }
    }
}