package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.R
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import org.json.JSONObject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navigateTo: NavigateTo) {
    var showDialog by remember { mutableStateOf(false) }

    val authViewModel = viewModel<AuthViewModel>()

    viewModel<MusicViewModel>().playMusic()



    LaunchedEffect(authViewModel.user) {
        if (authViewModel.user != null) {
            SocketIOManager.socket?.on("created-game") { args ->
                val gameId = (args[0] as JSONObject).getInt("gameId")
                navigateTo(Screen.WaitingForOpponent, mapOf("gameId" to gameId))
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            SocketIOManager.socket?.off("created-game")
        }
    }
    BackgroundBox(imageRes = R.drawable.crear_partida_background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("QuéTrucazo!", fontWeight = FontWeight.Bold, color = titleTopBarColor()) },
                    actions = {
                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = iconDialogColor())
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BotonImagen2(
                    onClick = {
                        SocketIOManager.createGame(authViewModel.user!!.id)
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    text = "CREAR PARTIDA"
                )

                Spacer(modifier = Modifier.height(16.dp))


                BotonImagen2(
                    onClick = {
                        navigateTo(Screen.GameList, emptyMap())
                    },
                    modifier = Modifier
                        .width(180.dp)
                        .height(40.dp),
                    text = "UNIRSE A UNA PARTIDA"
                )

                if (showDialog) {
                    VolumeControlDialog(onDismissRequest = { showDialog = false })
                }
            }
        }
    }
}