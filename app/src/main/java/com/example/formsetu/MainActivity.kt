package com.example.formsetu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.formsetu.data.model.AppDatabase
import com.example.formsetu.ui.navigation.AppNavGraph
import com.example.formsetu.ui.theme.FormSetuTheme

val LocalAppDatabase = staticCompositionLocalOf<AppDatabase> { error("No database provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "formsetu-db"
        ).build()
        setContent {
            FormSetuTheme {
                CompositionLocalProvider(LocalAppDatabase provides db) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        App()
                    }
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}


