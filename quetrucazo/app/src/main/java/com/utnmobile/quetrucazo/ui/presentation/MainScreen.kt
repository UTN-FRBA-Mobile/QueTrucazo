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
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var showDialog by remember { mutableStateOf(false) }

    viewModel<MusicViewModel>().playMusic()

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
            Button(onClick = { /* Handle Crear Partida */ }) {
                Text("Crear Partida")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { /* Handle Buscar Partida */ }) {
                Text("Buscar Partida")
            }

            if (showDialog) {
                VolumeControlDialog(onDismissRequest = { showDialog = false })
            }
        }
    }
}