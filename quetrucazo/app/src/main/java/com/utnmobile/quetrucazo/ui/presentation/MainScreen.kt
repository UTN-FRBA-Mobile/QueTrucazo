package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.model.toGames
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.connection.ConnectionViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import org.json.JSONArray
import org.json.JSONObject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navigateTo: NavigateTo) {
    var showDialog by remember { mutableStateOf(false) }

    val authViewModel = viewModel<AuthViewModel>()
    val connectionViewModel = viewModel<ConnectionViewModel>()

    val onConnect = onConnect@{
        SocketIOManager.socket?.on("join-game") { args ->
            val game = Game.from(args[0] as JSONObject)
            navigateTo(Screen.Game, mapOf("game" to game))
        }
        return@onConnect
    }

    val onDisconnect = onDisconnect@{
        // mostrar dialogo que diga "se perdió la conexión" y un boton que reinicie la app
        connectionViewModel.updateShowDisconnect(true)
    }

    SocketIOManager.connect(authViewModel.user!!.id, onConnect, onDisconnect)

    viewModel<MusicViewModel>().playMusic()

    SocketIOManager.socket?.on("created-game") { args ->
        val gameId = (args[0] as JSONObject).getInt("gameId")
        navigateTo(Screen.WaitingForOpponent, mapOf("gameId" to gameId))
    }

    DisposableEffect(Unit) {
        onDispose {
            SocketIOManager.socket?.off("created-game")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuéTrucazo!") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                SocketIOManager.createGame(authViewModel.user!!.id)
            }) {
                Text("Crear partida")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navigateTo(Screen.GameList, emptyMap()) }) {
                Text("Unirse a una partida")
            }

            if (showDialog) {
                VolumeControlDialog(onDismissRequest = { showDialog = false })
            }
        }
    }
}