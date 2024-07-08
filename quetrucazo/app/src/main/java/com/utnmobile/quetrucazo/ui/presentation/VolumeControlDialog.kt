package com.utnmobile.quetrucazo.ui.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel

@Composable
fun VolumeControlDialog(onDismissRequest: () -> Unit) {
    var musicVolume by remember { mutableFloatStateOf(0.5f) }
    var effectsVolume by remember { mutableFloatStateOf(0.5f) }
    val musicViewModel: MusicViewModel = viewModel()

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            color = dialogColor2()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = iconDialogColor())
                    }
                }
                Text("Música", style = MaterialTheme.typography.titleLarge, color = volumeColor())
                Slider(
                    value = musicVolume,
                    onValueChange = { newVolume ->
                        musicVolume = newVolume
                        musicViewModel.setVolume(newVolume)
                    },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = volumeColor(),
                        activeTrackColor = volumeColor(),
                        inactiveTrackColor = volumeColor()
                    )
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}