package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.ui.presentation.game.GameScreen
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel

typealias NavigateTo = (Screen, params: Map<String, Any>) -> Unit

@Composable
fun AppNavigation() {
    val musicViewModel: MusicViewModel = viewModel()
    var currentScreen by remember { mutableStateOf(Screen.Login) }
    var currentScreenParams by remember { mutableStateOf(emptyMap<String, Any>()) }

    val navigateTo: NavigateTo = { screen, params ->
        currentScreen = screen
        currentScreenParams = params
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
        Screen.WaitingForOpponent -> WaitingForOpponentScreen (navigateTo, currentScreenParams["gameId"] as Int)
        Screen.Game -> GameScreen(game = currentScreenParams["game"] as Game, navigateTo = navigateTo)
    }
}

enum class Screen {
    Login, Register, Main, GameList, WaitingForOpponent, Game
}