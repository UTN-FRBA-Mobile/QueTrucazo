package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.model.toGames
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import org.json.JSONArray
import org.json.JSONObject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(navigateTo: NavigateTo) {
    var showDialog by remember { mutableStateOf(false) }
    var games by remember { mutableStateOf(emptyList<Game>()) }
    var joiningGame by remember { mutableStateOf(false) }

    viewModel<MusicViewModel>().playMusic()
    val authViewModel = viewModel<AuthViewModel>()

    SocketIOManager.socket?.on("games-list") { args ->
        games = (args[0] as JSONArray).toGames()
    }

    SocketIOManager.socket?.on("join-game") { args ->
        val game = Game.from(args[0] as JSONObject)
        navigateTo(Screen.Game, mapOf("game" to game))
    }

    DisposableEffect(Unit) {
        onDispose {
            SocketIOManager.socket?.off("games-list")
            SocketIOManager.socket?.off("join-game")
        }
    }

    SocketIOManager.getGames()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuéTrucazo!") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigateTo(Screen.Main, emptyMap()) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(contentPadding = padding) {
                items(games) { game ->
                    ListItem(game, onClick = { gameId ->
                        if (joiningGame) return@ListItem
                        authViewModel.user?.let { user ->
                            joiningGame = true
                            SocketIOManager.joinGame(user.id, gameId)
                        }
                    })
                }
            }
        }
    )
}

@Composable
fun ListItem(game: Game, onClick: (gameId: Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { onClick(game.id) }),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = game.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}