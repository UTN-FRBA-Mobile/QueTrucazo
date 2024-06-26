package com.utnmobile.quetrucazo.ui.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.presentation.game.GameScreen
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.connection.ConnectionViewModel
import org.json.JSONObject

typealias NavigateTo = (Screen, params: Map<String, Any>) -> Unit

@Composable
fun AppNavigation(context: Context) {
    val authViewModel = viewModel<AuthViewModel>()
    val connectionViewModel = viewModel<ConnectionViewModel>()
    val showDisconnect by connectionViewModel.showDisconnect.collectAsState()

    var currentScreen by remember { mutableStateOf(Screen.Login) }
    var currentScreenParams by remember { mutableStateOf(emptyMap<String, Any>()) }

    val navigateTo: NavigateTo = { screen, params ->
        currentScreen = screen
        currentScreenParams = params
    }

    val onConnect = onConnect@{
        SocketIOManager.socket?.on("join-game") { args ->
            val game = Game.from(args[0] as JSONObject)
            navigateTo(Screen.Game, mapOf("game" to game))
        }
        return@onConnect
    }

    if (showDisconnect) {
        ConnectionLostDialog(
            onRestartApp = { restartApp(context) }
        )
    }

    LaunchedEffect(authViewModel.user) {
        if (authViewModel.user != null) {
            connectionViewModel.connect(authViewModel.user!!.id, onConnect)
            navigateTo(Screen.Main, emptyMap())
        }
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
}@Composable
fun ConnectionLostDialog(onRestartApp: () -> Unit) {
    AlertDialog(
        onDismissRequest = onRestartApp,
        title = { Text("Conexión perdida") },
        text = { Text("Se perdió la conexión. Por favor, reinicie la aplicación.") },
        confirmButton = {
            TextButton(onClick = onRestartApp) {
                Text("Reiniciar")
            }
        }
    )
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    context.startActivity(intent)
}