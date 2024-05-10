package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.runtime.*

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    when (currentScreen) {
        Screen.Login -> LoginScreen(
            onLogin = { username, password ->
                // Assume login is successful
                currentScreen = Screen.Main
            },
            onNavigateToRegister = { currentScreen = Screen.Register }
        )
        Screen.Register -> RegisterScreen(
            onRegister = { username, password ->
                // Assume registration is successful
                currentScreen = Screen.Main
            },
            onNavigateToLogin = { currentScreen = Screen.Login }
        )
        Screen.Main -> MainScreen()
    }
}

enum class Screen {
    Login, Register, Main
}