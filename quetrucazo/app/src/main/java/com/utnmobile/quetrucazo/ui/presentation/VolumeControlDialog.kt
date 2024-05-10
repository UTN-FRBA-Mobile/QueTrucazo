package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.ui.window.Dialog

@Composable
fun VolumeControlDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Música", style = MaterialTheme.typography.titleLarge)
                Slider(value = 0.5f, onValueChange = {}, modifier = Modifier.fillMaxWidth())
                Text(text = "Efectos", style = MaterialTheme.typography.titleLarge)
                Slider(value = 0.5f, onValueChange = {}, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(20.dp))
                Button(onClick = onDismissRequest) {
                    Text("Close")
                }
            }
        }
    }
}