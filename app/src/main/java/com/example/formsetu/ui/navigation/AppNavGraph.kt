package com.example.formsetu.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.formsetu.ui.screen.CameraOcrScreen
import com.example.formsetu.ui.screen.FormFillScreen
import com.example.formsetu.ui.screen.HomeScreen
import com.example.formsetu.ui.screen.SubmissionHistoryScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onFormSelected = { formFileName, language ->
                    navController.navigate("form/$formFileName/$language")
                },
                onHistoryClick = { navController.navigate("history") },
                onLanguageChange = { /* Handle language change */ }
            )
        }
        
        composable(
            route = "form/{formFileName}/{language}",
            arguments = listOf(
                navArgument("formFileName") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val formFileName = Uri.decode(backStackEntry.arguments?.getString("formFileName") ?: "forms/pan_form.json")
            val language = backStackEntry.arguments?.getString("language") ?: "hi"
            FormFillScreen(
                formFileName = formFileName,
                language = language,
                onFormSubmit = { /* Optional: Save or Export */ },
                onOpenCamera = { navController.navigate("camera") },
                onBackPressed = { navController.popBackStack() }
            )
        }
        
        composable("camera") {
            CameraOcrScreen(onClose = { navController.popBackStack() })
        }
        
        composable("history") {
            SubmissionHistoryScreen(onBack = { navController.popBackStack() })
        }
    }
}