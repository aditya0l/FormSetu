package com.example.formsetu.ui.component

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun AIHintHelper(
    guidanceText: String,
    speakLanguageCode: String = "hi"
) {
    var showTooltip by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Initialize TextToSpeech only once
    val tts by remember {
        mutableStateOf(TextToSpeech(context, null))
    }

    // Set language when TTS is ready
    LaunchedEffect(speakLanguageCode) {
        val locale = Locale(speakLanguageCode)
        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            println("TTS: Language not supported")
        }
    }

    Row(
        modifier = Modifier.padding(start = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = { showTooltip = !showTooltip }) {
            Icon(Icons.Filled.Help, contentDescription = "Help")
        }

        IconButton(onClick = {
            tts.speak(guidanceText, TextToSpeech.QUEUE_FLUSH, null, null)
        }) {
            Icon(Icons.Filled.VolumeUp, contentDescription = "Speak")
        }
    }

    if (showTooltip) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                text = guidanceText,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
