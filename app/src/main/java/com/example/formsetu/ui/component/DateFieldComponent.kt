package com.example.formsetu.ui.component

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateFieldComponent(
    value: String,
    label: String,
    isError: Boolean,
    errorText: String?,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                onDateSelected(formattedDate)
                showDialog = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)

        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Date") },
            trailingIcon = {
                Icon(
                    Icons.Default.DateRange, 
                    contentDescription = "Select Date",
                    modifier = Modifier.clickable { showDialog = true }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
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