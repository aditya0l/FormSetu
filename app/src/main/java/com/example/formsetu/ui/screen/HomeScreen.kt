package com.example.formsetu.ui.screen

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Badge
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

data class FormOption(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val formFileName: String,
    val color: androidx.compose.ui.graphics.Color
)

@Composable
fun HomeScreen(
    onFormSelected: (String, String) -> Unit,
    onHistoryClick: () -> Unit,
    onLanguageChange: (String) -> Unit = {}
) {
    var selectedLanguage by remember { mutableStateOf("hi") }
    
    val formOptions = listOf(
        FormOption(
            title = if (selectedLanguage == "hi") "पैन कार्ड" else "PAN Card",
            description = if (selectedLanguage == "hi") "पैन कार्ड के लिए आवेदन करें" else "Apply for PAN Card",
            icon = Icons.Default.CreditCard,
            formFileName = "forms/pan_form.json",
            color = MaterialTheme.colorScheme.primary
        ),
        FormOption(
            title = if (selectedLanguage == "hi") "पासपोर्ट" else "Passport",
            description = if (selectedLanguage == "hi") "पासपोर्ट के लिए आवेदन करें" else "Apply for Passport",
            icon = Icons.Default.Flight,
            formFileName = "forms/passport_form.json",
            color = MaterialTheme.colorScheme.tertiary
        ),
        FormOption(
            title = if (selectedLanguage == "hi") "आधार अपडेट" else "Aadhaar Update",
            description = if (selectedLanguage == "hi") "आधार विवरण अपडेट करें" else "Update Aadhaar Details",
            icon = Icons.Default.Badge,
            formFileName = "forms/aadhaar_form.json",
            color = MaterialTheme.colorScheme.secondary
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 0.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        // Welcoming header
        Text(
            text = "FormSetu",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = if (selectedLanguage == "hi") "सरकारी फ़ॉर्म भरना अब आसान!" else "Filling government forms made easy!",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onHistoryClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = "History",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    selectedLanguage = if (selectedLanguage == "hi") "en" else "hi"
                    onLanguageChange(selectedLanguage)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = if (selectedLanguage == "hi") "Switch to English" else "हिंदी में बदलें",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (selectedLanguage == "hi") "फॉर्म चुनें" else "Choose a Form",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            formOptions.forEachIndexed { idx, formOption ->
                AnimatedVisibility(visible = true, enter = fadeIn()) {
                    FormOptionCard(
                        formOption = formOption,
                        onClick = { onFormSelected(Uri.encode(formOption.formFileName), selectedLanguage) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (selectedLanguage == "hi") "विशेषताएं" else "Features",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                FeatureItem(
                    icon = Icons.Default.CameraAlt,
                    text = if (selectedLanguage == "hi") "OCR दस्तावेज़ स्कैनिंग" else "OCR Document Scanning"
                )
                FeatureItem(
                    icon = Icons.Default.Psychology,
                    text = if (selectedLanguage == "hi") "AI सहायता और मार्गदर्शन" else "AI Assistance & Guidance"
                )
                FeatureItem(
                    icon = Icons.Default.Verified,
                    text = if (selectedLanguage == "hi") "स्मार्ट वैलिडेशन" else "Smart Validation"
                )
                FeatureItem(
                    icon = Icons.Default.Download,
                    text = if (selectedLanguage == "hi") "PDF एक्सपोर्ट" else "PDF Export"
                )
            }
        }
    }
}

@Composable
private fun FormOptionCard(
    formOption: FormOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .defaultMinSize(minHeight = 80.dp),
        colors = CardDefaults.cardColors(
            containerColor = formOption.color.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = formOption.icon,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                tint = formOption.color
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formOption.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formOption.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 12.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 