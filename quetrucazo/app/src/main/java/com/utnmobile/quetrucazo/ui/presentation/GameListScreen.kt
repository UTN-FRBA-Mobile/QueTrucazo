package com.utnmobile.quetrucazo.ui.presentation

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
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import org.json.JSONArray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(navigateTo: NavigateTo) {
    var showDialog by remember { mutableStateOf(false) }
    var games by remember { mutableStateOf(emptyList<Game>()) }

    viewModel<MusicViewModel>().playMusic()

    SocketIOManager.socket?.on("games-list") { args ->
        games = (args[0] as JSONArray).toGames()
    }

    DisposableEffect(Unit) {
        onDispose {
            SocketIOManager.socket?.off("games-list")
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
                    IconButton(onClick = { navigateTo(Screen.Main) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(contentPadding = padding) {
                items(games) { game ->
                    ListItem(game)
                }
            }
        }
    )
}

@Composable
fun ListItem(game: Game) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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