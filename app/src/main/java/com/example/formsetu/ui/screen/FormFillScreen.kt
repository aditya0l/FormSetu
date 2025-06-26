package com.example.formsetu.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.formsetu.data.model.FormField
import com.example.formsetu.data.model.FormSchema
import com.example.formsetu.data.repository.FormRepository
import com.example.formsetu.ui.component.*
import com.example.formsetu.viewmodel.FormViewModel
import com.example.formsetu.data.model.FormSubmission
import com.example.formsetu.LocalAppDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFillScreen(
    formFileName: String = "forms/pan_form.json",
    language: String = "hi",
    onFormSubmit: (Map<String, String>) -> Unit = {},
    onOpenCamera: () -> Unit,
    onBackPressed: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: FormViewModel = viewModel()
    val db = LocalAppDatabase.current
    val gson = remember { Gson() }
    val snackbarHostState = remember { SnackbarHostState() }

    var formSchema by remember { mutableStateOf<FormSchema?>(null) }

    LaunchedEffect(Unit) {
        formSchema = FormRepository.loadFormSchema(context, formFileName)
    }

    LaunchedEffect(formFileName) {
        viewModel.resetForm()
        formSchema = FormRepository.loadFormSchema(context, formFileName)
    }

    val scrollState = rememberScrollState()

    if (formSchema == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = formSchema?.title?.get(language) ?: "Form",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onOpenCamera() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Scan Document", tint = Color.White)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            formSchema?.fields?.forEach { field ->
                val isRequired = field.required
                val label = buildString {
                    append(field.label[language] ?: field.key)
                    if (isRequired) append(" *")
                }
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    RenderFormField(
                        field = field,
                        language = language,
                        value = viewModel.fieldStates[field.key] ?: "",
                        error = viewModel.errorStates[field.key],
                        onValueChange = { viewModel.updateField(field.key, it) },
                        labelOverride = label,
                        isRequired = isRequired
                    )
                    AnimatedVisibility(visible = field.ai_guidance?.get(language) != null, enter = fadeIn()) {
                        field.ai_guidance?.get(language)?.let { tip ->
                            AIHintHelper(guidanceText = tip, speakLanguageCode = language)
                        }
                    }
                }
            }
            Button(
                onClick = {
                    val isValid = viewModel.validateForm(formSchema!!, language)
                    if (isValid) {
                        onFormSubmit(viewModel.fieldStates.toMap())
                        Log.d("FormSubmit", viewModel.fieldStates.toString())
                        // Save to Room
                        val formType = formSchema?.title?.get("en") ?: formFileName
                        val formDataJson = gson.toJson(viewModel.fieldStates)
                        val submission = FormSubmission(
                            formType = formType,
                            submissionDate = System.currentTimeMillis(),
                            formDataJson = formDataJson
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            val id = db.formSubmissionDao().insert(submission)
                            Log.d("FormSetu", "Saved submission with id: $id")
                            withContext(Dispatchers.Main) {
                                snackbarHostState.showSnackbar("Form saved!")
                            }
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar("Please fill all required fields.")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(if (language == "hi") "सबमिट करें" else "Submit")
            }
        }
    }
}

@Composable
private fun RenderFormField(
    field: FormField,
    language: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    labelOverride: String? = null,
    isRequired: Boolean = false
) {
    val label = labelOverride ?: field.label[language] ?: field.key
    val hint = field.hint?.get(language)
    when (field.type.lowercase()) {
        "text" -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Row {
                        Text(label)
                        if (isRequired) Text(" *", color = Color.Red)
                    }
                },
                placeholder = { if (hint != null) Text(hint) },
                isError = error != null,
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
            )
            AnimatedVisibility(visible = error != null, enter = fadeIn()) {
                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }
            }
        }
        "dropdown" -> {
            DropdownFieldComponent(
                selectedOption = value,
                label = label,
                options = field.options ?: emptyList(),
                isError = error != null,
                errorText = error,
                onOptionSelected = onValueChange
            )
        }
        "date" -> {
            DateFieldComponent(
                value = value,
                label = label,
                isError = error != null,
                errorText = error,
                onDateSelected = onValueChange
            )
        }
        else -> {
            Text("Unsupported field type: ${field.type}")
        }
    }
}