package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PlayGameScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        Button(onClick = { /* Handle Truco */ }) {
            Text("Truco")
        }

        Button(onClick = { /* Handle Envido */ }) {
            Text("Envido")
        }

        Button(onClick = { /* Handle Other Action */ }) {
            Text("Other Action")
        }
    }
}