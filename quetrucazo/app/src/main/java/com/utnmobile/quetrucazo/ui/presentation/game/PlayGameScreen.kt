package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun PlayGameScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val buttonColors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))

        Button(
            onClick = { /* Handle Truco */ },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = buttonColors
        ) {
            Text("TRUCO")
        }

        Button(
            onClick = { /* Handle Envido */ },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = buttonColors
        ) {
            Text("ENVIDO")
        }

        Button(
            onClick = { /* Handle Mazo */ },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = buttonColors
        ) {
            Text("MAZO")
        }
    }
}




