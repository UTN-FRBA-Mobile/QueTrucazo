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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun PlayGameScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()  // Ensures the Row takes up all the horizontal space
            .padding(0.dp),  // Set padding to zero to ensure no extra space on sides
        horizontalArrangement = Arrangement.SpaceEvenly,  // Ensures buttons are evenly spaced across the width
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Define the button style
        val buttonColors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))

        // TRUCO Button
        Button(
            onClick = { /* Handle Truco */ },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),  // Ensures each button stretches to fill the assigned space
            shape = RectangleShape,  // Ensures the buttons are rectangular
            colors = buttonColors
        ) {
            Text("TRUCO")
        }

        // ENVIDO Button
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

        // MAZO Button
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




