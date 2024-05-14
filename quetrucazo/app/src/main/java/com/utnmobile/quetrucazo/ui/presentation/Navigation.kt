package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel

typealias NavigateTo = (Screen) -> Unit

@Composable
fun AppNavigation() {
    val musicViewModel: MusicViewModel = viewModel()
    var currentScreen by remember { mutableStateOf(Screen.Login) }

    val navigateTo: NavigateTo = { screen ->
        currentScreen = screen
    }

    when (currentScreen) {
        Screen.Login -> LoginScreen(
            navigateTo = navigateTo,
        )
        Screen.Register -> RegisterScreen(
            navigateTo = navigateTo,
        )
        Screen.Main -> MainScreen(navigateTo)
        Screen.GameList -> GameListScreen(navigateTo)
        Screen.WaitingForOpponent -> WaitingForOpponentScreen (navigateTo)
    }
}

enum class Screen {
    Login, Register, Main, GameList, WaitingForOpponent,
}