package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.R
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
    BackgroundBox(imageRes = R.drawable.crear_partida_background) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Surface(
                    color = Color.Black.copy(alpha = 0.6f), // Color de fondo semi-transparente
                    shape = MaterialTheme.shapes.small, // Forma del fondo
                    modifier = Modifier.padding(horizontal = 8.dp) // Espaciado alrededor del texto
                ) {
                    Text(
                        "Esperando rival...",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                CircularProgressIndicator(color = volumeColor())

                Spacer(modifier = Modifier.height(64.dp))

                Button(
                    onClick = {
                        authViewModel.user?.let { user ->
                            SocketIOManager.cancelGame(user.id, gameId)
                            navigateTo(Screen.Main, emptyMap())
                        }
                    },
                    colors = colorBoton()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}