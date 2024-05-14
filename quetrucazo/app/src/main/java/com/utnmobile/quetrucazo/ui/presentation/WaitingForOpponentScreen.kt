package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import org.json.JSONObject


@Composable
fun WaitingForOpponentScreen(navigateTo: NavigateTo, gameId: Int) {
    val authViewModel = viewModel<AuthViewModel>()

    SocketIOManager.socket?.on("cancelled-game") {
        navigateTo(Screen.Main, emptyMap())
    }

    SocketIOManager.socket?.on("join-game") { args ->
        val game = Game.from(args[0] as JSONObject)
        navigateTo(Screen.Game, mapOf("game" to game))
    }

    DisposableEffect(Unit) {
        onDispose {
            SocketIOManager.socket?.off("cancelled-game")
            SocketIOManager.socket?.off("join-game")
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Esperando rival...",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(64.dp))

            Button(onClick = {
                authViewModel.user?.let { user ->
                    SocketIOManager.cancelGame(user.id, gameId)
                    navigateTo(Screen.Main, emptyMap())
                }
            }) {
                Text("Cancelar")
            }
        }
    }
}