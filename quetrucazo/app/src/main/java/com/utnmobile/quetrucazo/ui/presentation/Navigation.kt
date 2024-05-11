package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.runtime.*
import com.utnmobile.quetrucazo.ui.music.MusicViewModel

typealias OnNavigateTo = (Screen) -> Unit

@Composable
fun AppNavigation(musicViewModel: MusicViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.Login) }

    val onNavigateTo: OnNavigateTo = { screen ->
        currentScreen = screen
    }

    when (currentScreen) {
        Screen.Login -> LoginScreen(
            onLogin = { username, password ->
                currentScreen = Screen.Main
                musicViewModel.playMusic()
            },
            onNavigateTo = onNavigateTo,
        )
        Screen.Register -> RegisterScreen(
            onRegister = { username, password ->
                currentScreen = Screen.Main
                musicViewModel.playMusic()
            },
            onNavigateTo = onNavigateTo,
        )
        Screen.Main -> MainScreen(musicViewModel)
    }
}

enum class Screen {
    Login, Register, Main
}