package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.runtime.*
import com.utnmobile.quetrucazo.music.MusicModel

@Composable
fun AppNavigation(musicViewModel: MusicModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    when (currentScreen) {
        Screen.Login -> LoginScreen(
            onLogin = { username, password ->
                currentScreen = Screen.Main
                musicViewModel.playMusic()
            },
            onNavigateToRegister = { currentScreen = Screen.Register }
        )
        Screen.Register -> RegisterScreen(
            onRegister = { username, password ->
                currentScreen = Screen.Main
                musicViewModel.playMusic()
            },
            onNavigateToLogin = { currentScreen = Screen.Login }
        )
        Screen.Main -> MainScreen(musicViewModel)
    }
}

enum class Screen {
    Login, Register, Main
}