package com.utnmobile.quetrucazo.ui.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    Dialog(
        onDismissRequest = onRestartApp
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            color = dialogColor()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Conexión perdida",
                        style = TextStyle(
                            fontSize = 30.sp,
                            color = dialogTextColor(),
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.animateContentSize()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {

                    BotonImagen2(
                        onClick = {
                            onRestartApp()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        text = "RECONECTAR"
                    )

                }

            }
        }
    }
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    context.startActivity(intent)
}