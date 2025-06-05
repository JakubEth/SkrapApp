package com.example.SkrapApp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.SkrapApp.ui.onboarding.OnboardingScreen
import com.example.SkrapApp.ui.webview.WebViewScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(
                onGuestClick = { /* ... */ },
                onLoginClick = { /* ... */ },
                onShowRegulamin = { navController.navigate("webview_regulamin") },
                onShowPolityka = { navController.navigate("webview_polityka") }
            )
        }
        composable("webview_regulamin") {
            WebViewScreen(url = "https://youtube.com")
        }
        composable("webview_polityka") {
            WebViewScreen(url = "https://google.com")
        }
        // Dodawaj kolejne ekrany tutaj
    }
}
