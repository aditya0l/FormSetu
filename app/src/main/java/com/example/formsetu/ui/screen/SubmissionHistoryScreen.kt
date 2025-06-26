package com.example.formsetu.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.formsetu.LocalAppDatabase
import com.example.formsetu.data.model.FormSubmission
import com.example.formsetu.data.repository.FormRepository
import com.example.formsetu.util.PDFExporter
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionHistoryScreen(onBack: () -> Unit) {
    val db = LocalAppDatabase.current
    val coroutineScope = rememberCoroutineScope()
    var submissions by remember { mutableStateOf<List<FormSubmission>>(emptyList()) }
    val gson = remember { Gson() }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        submissions = db.formSubmissionDao().getAllSubmissions()
        Log.d("FormSetu", "Loaded ${submissions.size} submissions from DB")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Submission History", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (submissions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No submissions yet.",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(submissions) { submission ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    submission.formType,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    dateFormat.format(Date(submission.submissionDate)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val schemaFileName = when (submission.formType) {
                                            "PAN Card Application (Form 49A)" -> "forms/pan_form.json"
                                            "Aadhaar Update Form (UIDAI Form 5)" -> "forms/aadhaar_form.json"
                                            "Passport Application Form" -> "forms/passport_form.json"
                                            else -> "forms/pan_form.json"
                                        }
                                        val schema = FormRepository.loadFormSchema(context, schemaFileName)
                                        if (schema != null) {
                                            val formData: Map<String, String> = gson.fromJson(submission.formDataJson, Map::class.java) as Map<String, String>
                                            PDFExporter.exportFormToPdf(context, schema, formData, "hi")
                                            snackbarHostState.showSnackbar("PDF exported!")
                                        } else {
                                            snackbarHostState.showSnackbar("Could not load form schema for export.")
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.PictureAsPdf,
                                    contentDescription = "Export to PDF",
                                    tint = Color(0xFFD32F2F)
                                )
                            }
                        }
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
} 